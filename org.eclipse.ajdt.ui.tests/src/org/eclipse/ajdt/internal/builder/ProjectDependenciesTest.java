/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Helen Hawkins   - iniital version
 *******************************************************************************/
package org.eclipse.ajdt.internal.builder;

import junit.framework.TestCase;

import org.eclipse.ajdt.internal.core.AJDTUtils;
import org.eclipse.ajdt.internal.core.AJDTUtilsTest.MyJobChangeListener;
import org.eclipse.ajdt.internal.ui.preferences.AspectJPreferences;
import org.eclipse.ajdt.test.utils.BlockingProgressMonitor;
import org.eclipse.ajdt.test.utils.PluginTestProject;
import org.eclipse.ajdt.test.utils.Utils;
import org.eclipse.ajdt.ui.AspectJUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author hawkinsh
 *  
 */
public class ProjectDependenciesTest extends TestCase {

	BlockingProgressMonitor monitor;

	protected void setUp() throws Exception {
		super.setUp();
		Utils.blockPreferencesConfigWizard();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		Utils.restoreBlockedSettings();
	}

	/**
	 * Because of the changes in how we deal with dependencies between projects
	 * (see the manual testing/project dependencies/The history of project
	 * dependencies.txt file for more information), we need a test which spells
	 * out which one we're using. This is purely to detect whether we're:
	 * 
	 * a) changing project dependencies to class folder dependencies or, b)
	 * leaving project dependencies alone.
	 * 
	 * If this test fails, then other asserts need to be switched (some are
	 * marked with a PD in the comment).
	 */
	public void testHowDealingWithProjectDependencies() throws Exception {
		IProject projectY = Utils.getPredefinedProject("project.java.Y", true);
		projectY.build(IncrementalProjectBuilder.FULL_BUILD, null);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		IProject projectX = Utils.getPredefinedProject("project.java.X", true);
		projectX.build(IncrementalProjectBuilder.FULL_BUILD, null);
		ProjectDependenciesUtils.waitForJobsToComplete(projectX);
		// sanity check: at this point there should be no error markers, both
		// projects should build as they're both java projects, project X should
		// should have a project dependency on project Y
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,
						null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,
						null));
		assertTrue("project X should have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,
						projectY));

		// convert project Y to be an AJ project and check setup is correct
		AJDTUtils.addAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,
						null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,
						null));

		// PD: The following is how we know how we're dealing with Project
		// dependencies...

		// If we're changing project dependencies to class folder dependencies
		// (both when
		// AJ nature is added and when an AJ build is done) then the following
		// two asserts
		// should be uncommented
		//assertFalse("project X should not have a project dependency on
		// project Y",
		//		projectHasProjectDependency(projectX, projectY));
		//assertTrue("project X should have a class folder dependency on
		// project Y",
		//		projectHasClassFolderDependency(projectX,projectY));

		// If we're not touching project dependencies when AJ nature is
		// added/removed
		// then the following two asserts should be uncommented.
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,
						projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(
						projectX, projectY));

		// remove AJ nature from Y and the following asserts should always be
		// true
		AJDTUtils.removeAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,
						null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,
						null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,
						projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(
						projectX, projectY));

		BlockingProgressMonitor monitor = new BlockingProgressMonitor();
		monitor.reset();
		projectX.close(monitor);
		monitor.waitForCompletion();
		monitor.reset();
		projectY.close(monitor);
		monitor.waitForCompletion();
	}

	/**
	 * Test project dependencies work with plugin projects i.e. have two plugin
	 * projects, A and B where A depends on B. Then convert project B to be an
	 * AspectJ project - both projects should still build. In particular,
	 * project A should not have any error markers from the java builder saying
	 * something along the lines of "please rebuild prerequisite project B" (Bug
	 * 70288)
	 */
	public void testProjectDependenciesWithPluginProjects() throws Exception {
		setUpPluginEnvironment();
		MyJobChangeListener listener = new MyJobChangeListener();
		Platform.getJobManager().addJobChangeListener(listener);

		// create two plugin projects (java)
		PluginTestProject projectA = new PluginTestProject("MyPluginProjectA",
				"another.test.pluginA", "ProjectAPlugin", "projectA.jar");
		PluginTestProject projectB = new PluginTestProject("MyPluginProjectB",
				"another.test.pluginB", "ProjectBPlugin", "projectB.jar");

		// set up plugin dependency where project A depends on project B
		// (there should be no build errors)
		ProjectDependenciesUtils.addPluginDependency(projectA.getProject(), projectB.getPluginID(),new BlockingProgressMonitor());
		assertFalse("project A should not have any IJavaModelMarkers",
				ProjectDependenciesUtils.projectMarkedWithPrereqMessage(
						projectA.getProject(), projectB.getProject()));

		// convert project B to be an AJ project ==> still no build errors
		AJDTUtils.addAspectJNature(projectB.getProject());
		ProjectDependenciesUtils.waitForJobsToComplete(projectB.getProject());
		ProjectDependenciesUtils.waitForJobsToComplete(projectA.getProject());
		assertFalse("project A should still not have any IJavaModelMarkers",
				ProjectDependenciesUtils.projectMarkedWithPrereqMessage(
						projectA.getProject(), projectB.getProject()));

		projectA.dispose();
		projectB.dispose();
		resetPluginEnvironment();
	}

	/**
	 * This creates two projects and then adds and remove AJ nature to them in
	 * various sequences - ensuring that the dependencies remain as they should
	 */
	public void testProjectDependencies1() throws Exception {
		IProject projectY = Utils.getPredefinedProject("project.java.Y", true);
		projectY.build(IncrementalProjectBuilder.FULL_BUILD, null);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		IProject projectX = Utils.getPredefinedProject("project.java.X", true);
		projectX.build(IncrementalProjectBuilder.FULL_BUILD, null);
		ProjectDependenciesUtils.waitForJobsToComplete(projectX);
		// sanity check: at this point there should be no error markers, both
		// projects should build as they're both java projects, project X should
		// should have a project dependency on project Y
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,
						null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,
						null));
		assertTrue("project X should have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,
						projectY));

		// convert project Y to be an AJ project and check setup is correct
		AJDTUtils.addAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));

		// convert project X to be an AJ project and check setup is correct
		AJDTUtils.addAspectJNature(projectX);
		ProjectDependenciesUtils.waitForJobsToComplete(projectX);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));
		
		// remove AJ nature from project Y		
		AJDTUtils.removeAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));

		// convert project Y to be an AJ project and check setup is correct
		AJDTUtils.addAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));

		// remove AJ nature from project X		
		AJDTUtils.removeAspectJNature(projectX);
		ProjectDependenciesUtils.waitForJobsToComplete(projectX);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));
		
		// remove AJ nature from project Y		
		AJDTUtils.removeAspectJNature(projectY);
		ProjectDependenciesUtils.waitForJobsToComplete(projectY);
		assertFalse("project Y should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectY,null));
		assertFalse("project X should build with no errors",
				ProjectDependenciesUtils.projectIsMarkedWithError(projectX,null));
		assertTrue(
				"project X should still have a project dependency on project Y",
				ProjectDependenciesUtils.projectHasProjectDependency(projectX,projectY));
		assertFalse(
				"project X should NOT have a class folder dependency on project Y",
				ProjectDependenciesUtils.projectHasClassFolderDependency(projectX, projectY));
		
		BlockingProgressMonitor monitor = new BlockingProgressMonitor();
		monitor.reset();
		projectX.close(monitor);
		monitor.waitForCompletion();
		monitor.reset();
		projectY.close(monitor);
		monitor.waitForCompletion();
	}

	private void setUpPluginEnvironment() throws CoreException {
		// set the project up so that when asked, the pde plugin
		// is added automatically and the preference configurations
		// have all been set up (therefore don't need user
		// interaction.
		AspectJPreferences.setAskPDEAutoImport(false);
		AspectJPreferences.setDoPDEAutoImport(true);
		AspectJPreferences.setPDEAutoImportConfigDone(true);
	}

	private void resetPluginEnvironment() {
		IPreferenceStore ps = AspectJUIPlugin.getDefault().getPreferenceStore();
		ps.setToDefault(AspectJPreferences.PDE_AUTO_IMPORT_CONFIG_DONE);
		ps.setToDefault(AspectJPreferences.ASK_PDE_AUTO_IMPORT);
		ps.setToDefault(AspectJPreferences.DO_PDE_AUTO_IMPORT);
		ps.setToDefault(AspectJPreferences.AJDT_PREF_CONFIG_DONE);
	}


}

