/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ben Dalziel     - initial version
 *******************************************************************************/
package org.eclipse.ajdt.internal.ui.text;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 */
public class UIMessages extends NLS {

	private static final String BUNDLE_NAME= UIMessages.class.getName();

	private UIMessages() {
		// Do not instantiate
	}
	
	public static String ajde_version;
	public static String test_message;
	public static String configFileCreateError;
	public static String suspiciouslyLongCompileDialog;
	public static String isYourProjectReallyBig;
	public static String buildPrereqsMessage;
	public static String Builder_migration_build;
	public static String Builder_migration_title;
	public static String Builder_migration_message;
	public static String Builder_migration_toggle;
	public static String Builder_migration_failed_title;
	public static String Builder_migration_failed_message;
	public static String AllTypesUpdateJob;
	public static String UpdatedTypesCache;
	public static String ajCompilation;
	public static String builderPreferences;
	public static String compilerOptions;
	public static String useAJOutline;
	public static String codeTemplates_couldNotLoad;
	public static String ajAboutDialog;
	public static String ajWarningDialogTitle;
	public static String ajErrorDialogTitle;
	public static String jmCoreException;
	public static String noOutputDir;
	public static String newConfig;
	public static String BuildConfig_createLstFile;
	public static String BuildConfig_createLstDesc;
	public static String BuildConfig_includeAllSource;
	public static String BuildConfig_openForEdit;
	public static String BuildConfig_activate;
	public static String BuildConfig_newLstFile;
	public static String BuildConfig_needToSelectProject;
	public static String buildOptionsAdapter_exceptionInitializingProperties;
	public static String NewAspectjProjectCreationWizard_title;
	public static String NewAspectjProjectCreationWizard;
	public static String NewAspectjProjectCreationWizard_MainPage_title;
	public static String NewAspectjProjectCreationWizard_MainPage_description;
	public static String NewAspectjProjectCreationWizard_build_error_title;
	public static String NewAspectjProjectCreationWizard_build_error_message;
	public static String NewAspectjProjectCreationWizard_op_error_title;
	public static String NewAspectjProjectCreationWizard_op_error_message;
	public static String NewAspectjProjectCreationWizard_MainPage_project_exists;
	public static String NewAspectjProjectCreationWizard_MainPage_project_exists_capitalization;
	public static String EditorRulerContextMenu_adviceInAffect;
	public static String EditorRulerContextMenu_adviceAffects;
	public static String EditorRulerContextMenu_aspectDeclarations;
	public static String EditorRulerContextMenu_declaredOn;
	public static String EditorRulerContextMenu_annotations;
	public static String EditorRulerContextMenu_annotationAffects;
	public static String EditorRulerContextMenu_relatedLocations;
	public static String EditorRulerContextMenu_relatedLocation_message;
	public static String PluginImportDialog_importConfirmTitle;
	public static String PluginImportDialog_importConfirmMsg;
	public static String PluginImportDialog_importConfirmToggleMsg ;
	public static String NoAutoPluginImportDialog_title;
	public static String NoAutoPluginImportDialog_message;
	public static String AutoPluginImportDialog_noEditor_title;
	public static String AutoPluginImportDialog_noEditor_message;
	public static String AutoPluginRemoveDialog_noEditor_title;
	public static String AutoPluginRemoveDialog_noEditor_message;
	public static String AutoPluginImportErrorDialog_title;
	public static String AutoPluginImportErrorDialog_message;
	public static String AutoPluginRemoveErrorDialog_title;
	public static String AutoPluginRemoveErrorDialog_message;
	public static String PluginImportDialog_removeImportConfirmTitle;
	public static String PluginImportDialog_removeImportConfirmMsg;
	public static String PluginImportDialog_removeImportConfirmToggleMsg;
	public static String RefactorDialog_Move_ContainsAspectsFatalError;
	public static String RefactorDialog_Rename_ContainsAspectsFatalError;
	public static String NewAspectJProject_CreateAnAspectJProject;
	public static String NewAspectJProject_CreateAnAspectJProjectDescription;
	public static String NewAspectJProject_BuildSettings;
	public static String NewAspectJProject_BuildSettingsDescription;
	public static String NewTypeWizardPage_SuperClassDialog_title;
	public static String NewTypeWizardPage_SuperClassDialog_message;
	public static String PluginExportWizard_31Title;
	public static String MultipleOutputDirs_title;
	public static String MultipleOutputDirs_message;
	public static String compileReqd;
	public static String JavaOutlinePage_ContextMenu_refactoring_label;
	public static String JavaOutlinePage_HideFields_description_checked;
	public static String JavaOutlinePage_HideFields_description_unchecked;
	public static String JavaOutlinePage_HideFields_label;
	public static String JavaOutlinePage_HideFields_tooltip_checked;
	public static String JavaOutlinePage_HideFields_tooltip_unchecked;
	public static String JavaOutlinePage_HideNonePublicMembers_description_checked;
	public static String JavaOutlinePage_HideNonePublicMembers_description_unchecked;
	public static String JavaOutlinePage_HideNonePublicMembers_label;
	public static String JavaOutlinePage_HideNonePublicMembers_tooltip_checked;
	public static String JavaOutlinePage_HideNonePublicMembers_tooltip_unchecked;
	public static String JavaOutlinePage_HideStaticMembers_description_checked;
	public static String JavaOutlinePage_HideStaticMembers_description_unchecked;
	public static String JavaOutlinePage_HideStaticMembers_label;
	public static String JavaOutlinePage_HideStaticMembers_tooltip_checked;
	public static String JavaOutlinePage_HideStaticMembers_tooltip_unchecked;
	public static String JavaOutlinePage_Sort_description_checked;
	public static String JavaOutlinePage_Sort_description_unchecked;
	public static String JavaOutlinePage_Sort_label;
	public static String JavaOutlinePage_Sort_tooltip_checked;
	public static String JavaOutlinePage_Sort_tooltip_unchecked;
	public static String JavaOutlinePage_error_ChildrenProvider_getChildren_message1;
	public static String JavaOutlinePage_error_ChildrenProvider_hasChildren_message1;
	public static String AJavaOutlinePage_HidePointcuts_description_checked;
	public static String AJavaOutlinePage_HidePointcuts_description_unchecked;
	public static String AJavaOutlinePage_HidePointcuts_label;
	public static String AJavaOutlinePage_HidePointcuts_tooltip_checked;
	public static String AJavaOutlinePage_HidePointcuts_tooltip_unchecked;
	public static String AJavaOutlinePage_HideAdvice_description_checked;
	public static String AJavaOutlinePage_HideAdvice_description_unchecked;
	public static String AJavaOutlinePage_HideAdvice_label;
	public static String AJavaOutlinePage_HideAdvice_tooltip_checked;
	public static String AJavaOutlinePage_HideAdvice_tooltip_unchecked;
	public static String AJavaOutlinePage_HideIntroductions_description_checked;
	public static String AJavaOutlinePage_HideIntroductions_description_unchecked;
	public static String AJavaOutlinePage_HideIntroductions_label;
	public static String AJavaOutlinePage_HideIntroductions_tooltip_checked;
	public static String AJavaOutlinePage_HideIntroductions_tooltip_unchecked;
	public static String AJavaOutlinePage_HideDeclarations_description_checked;
	public static String AJavaOutlinePage_HideDeclarations_description_unchecked;
	public static String AJavaOutlinePage_HideDeclarations_label;
	public static String AJavaOutlinePage_HideDeclarations_tooltip_checked;
	public static String AJavaOutlinePage_HideDeclarations_tooltip_unchecked;
	public static String bad_project;
	public static String newResourcesWizard_pageDescription;
	public static String newResourcesWizard_pageTitle;
	public static String newResourcesWizard_instructions;
	public static String newResourcesWizard_selectAllResources;
	public static String newResourcesWizard_selectAllConfigurations;
	public static String newResourcesWizard_deselectAllResources;
	public static String newResourcesWizard_deselectAllConfigurations;
	public static String newResourcesWizard_wizardTitle;
	public static String newResourcesWizard_exceptionAppendingToBuildConfigFile;
	public static String NewAspectCreationWizard_title;
	public static String NewAspectCreationWizardPage_title;
	public static String NewAspectCreationWizardPage_description;
	public static String NewAspectCreationWizardPage_sourceFolder_label;
	public static String NewAspectCreationWizardPage_sourceFolder_button;
	public static String NewAspectCreationWizardPage_package_label;
	public static String NewAspectCreationWizardPage_package_button;
	public static String NewAspectCreationWizardPage_enclosingType_label;
	public static String NewAspectCreationWizardPage_enclosingType_button;
	public static String NewAspectCreationWizardPage_typeName_label;
	public static String NewAspectCreationWizardPage_modifiers_label;
	public static String NewAspectCreationWizardPage_supertype_label;
	public static String NewAspectCreationWizardPage_interfaces_label;
	public static String NewAspectCreationWizardPage_interfaces_add_button;
	public static String NewAspectCreationWizardPage_interfaces_remove_button;
	public static String NewAspectCreationWizardPage_methodStubs_label;
	public static String NewAspectCreationWizardPage_Name_field_cannot_be_empty;
	public static String NewAspectCreationWizardPage_Name_field_cannot_be_qualified;
	public static String NewAspectCreationWizardPage_Source_folder_cannot_be_empty;
	public static String NewAspectCreationWizardPage_Source_folder_does_not_exist;
	public static String NewAspectCreationWizardPage_error_EnclosingTypeEnterName;
	public static String NewAspectCreationWizardPage_error_EnclosingTypeNotExists;
	public static String NewAspectCreationWizardPage_error_EnclosingNotInCU;
	public static String NewAspectCreationWizardPage_error_EnclosingNotEditable;
	public static String NewAspectCreationWizardPage_warning_EnclosingNotInSourceFolder;
	public static String NewAspectCreationWizardPage_ChooseEnclosingTypeDialog_title;
	public static String NewAspectCreationWizardPage_ChooseEnclosingTypeDialog_description;
	public static String NewAspectCreationWizardPage_SuperInterfaceSelectionDialog_interfaceadded_info;
	public static String NewAspectCreationWizardPage_InterfacesDialog_class_title;
	public static String NewAspectCreationWizardPage_InterfacesDialog_message;
	public static String NewAspectCreationWizardPage_FolderSelectionDialog_title;
	public static String NewAspectCreationWizardPage_FolderSelectionDialog_message;
	public static String NewAspectCreationWizardPage_ChoosePackageDialog_title;
	public static String NewAspectCreationWizardPage_ChoosePackageDialog_message;
	public static String NewAspectCreationWizardPage_ChoosePackageDialog_empty;
	public static String NewAspectCreationWizardPage_CreatingAspect_message;
	public static String NewAspectCreationWizardPage_instantiation_label;
	public static String AJDTPrefConfigWizard_title;
	public static String AJDTPrefConfigWizardPage_name;
	public static String AJDTPrefConfigWizardPage_title;
	public static String AJDTPrefConfigWizardPage_description;
	public static String AJDTPrefConfigWizardPage_workbench_group;
	public static String AJDTPrefConfigWizardPage_workbench_ajeditordefault;
	public static String AJDTPrefConfigWizardPage_workbench_unusedimports;
	public static String AJDTPrefConfigWizardPage_workbench_analyzeannotations;
	public static String AJDTPrefConfigWizardPage_workbench_askagain;
	public static String AJDTPrefConfigWizardPage_editorMessage;
	public static String AJDTPrefConfigWizardPage_annotationsMessage;
	public static String AJDTPrefConfigWizardPage_workbench_openXRefView;
	public static String AJDTPrefConfigWizardPage_ErrorOpeningXRefView;
	public static String ajEditor;
	public static String AspectJEditor_runtimetest;
	public static String AspectJMarkersAtLine;
	public static String compilerPropsPage_description;
	public static String compilerPropsPage_useJavacMode;
	public static String compilerPropsPage_preprocessMode;
	public static String compilerPropsPage_workingDirPath;
	public static String compilerPropsPage_characterEncoding;
	public static String compilerPropsPage_source14;
	public static String compilerPropsPage_portingMode;
	public static String compilerPropsPage_compilerStrictness;
	public static String compilerPropsPage_compilerStrictnessNormal;
	public static String compilerPropsPage_compilerStrictnessLenient;
	public static String compilerPropsPage_compilerStrictnessStrict;
	public static String compilerPropsPage_nonStandardOptions;
	public static String compilerPropsPage_exceptionDuringRetrieve;
	public static String compilerPropsPage_exceptionDuringStore;
	public static String compilerPropsPage_exceptionDefaultingProperties;
	public static String compilerPropsPage_exceptionInitializingProperties;
	public static String compilerPropsPage_useIncrementalCompiler;
	public static String compilerPropsPage_inputJars;
	public static String compilerPropsPage_outputJar;
	public static String compilerPropsPage_sourceRoots;
	public static String compilerPropsPage_aspectJars;
	public static String compilerPropsPage_browseLabel;
	public static String compilerPropsPage_incrementalCompilerStatus;
	public static String compilerPropsPage_buildasm;
	public static String compilerPropsPage_buildasm_details;
	public static String compilerPropsPage_showweavemessages;
	public static String compilerPropsPage_showweavemessages_details;
	public static String aspectjPreferences_description;
	public static String aspectjPreferences_compilerVersion;
	public static String aspectjPreferences_pluginVersion;
	public static String aspectjPreferences_autobuildSuppressed;
	public static String aspectjPreferences_fileExt;
	public static String aspectjpreferences_fileExt_details;
	public static String aspectjPreferences_adviceDec;
	public static String CompilerConfigurationBlock_error;
	public static String CompilerConfigurationBlock_warning;
	public static String CompilerConfigurationBlock_ignore;
	public static String CompilerConfigurationBlock_needsbuild_title;
	public static String CompilerConfigurationBlock_needsfullbuild_message;
	public static String CompilerConfigurationBlock_needsprojectbuild_message;
	public static String OptionsConfigurationBlock_builderror_title;
	public static String OptionsConfigurationBlock_builderror_message;
	public static String OptionsConfigurationBlock_buildall_taskname;
	public static String OptionsConfigurationBlock_buildproject_taskname;
	public static String CompilerConfigurationBlock_aspectj_tabtitle;
	public static String AJCompilerPreferencePage_title;
	public static String AJCompilerPreferencePage_description;
	public static String CompilerPreferencePage_description;
	public static String CompilerPropertyPage_useworkspacesettings_label;
	public static String CompilerPropertyPage_useworkspacesettings_change;
	public static String CompilerPropertyPage_useprojectsettings_label;
	public static String CompilerConfigurationBlock_aj_messages_tabtitle;
	public static String CompilerConfigurationBlock_aj_messages_description;
	public static String CompilerConfigurationBlock_aj_invalid_absolute_type_name_label;
	public static String CompilerConfigurationBlock_aj_invalid_wildcard_type_name_label;
	public static String CompilerConfigurationBlock_aj_unresolvable_member_label;
	public static String CompilerConfigurationBlock_aj_type_not_exposed_to_weaver_label;
	public static String CompilerConfigurationBlock_aj_shadow_not_in_structure_label;
	public static String CompilerConfigurationBlock_aj_unmatched_super_type_in_call_label;
	public static String CompilerConfigurationBlock_aj_cannot_implement_lazy_tjp_label;
	public static String CompilerConfigurationBlock_aj_need_serial_version_uid_field_label;
	public static String CompilerConfigurationBlock_aj_incompatible_serial_version_label;
	public static String CompilerConfigurationBlock_aj_no_interface_ctor_joinpoint_label;
	public static String CompilerConfigurationBlock_aj_advanced_tabtitle;
	public static String CompilerConfigurationBlock_aj_advanced_description;
	public static String CompilerConfigurationBlock_aj_no_weave_label;
	public static String CompilerConfigurationBlock_aj_x_serializable_aspects_label;
	public static String CompilerConfigurationBlock_aj_x_lazy_tjp_label;
	public static String CompilerConfigurationBlock_aj_x_no_inline_label;
	public static String CompilerConfigurationBlock_aj_x_reweavable_label;
	public static String CompilerConfigurationBlock_aj_x_reweavable_compress_label;
	public static String CompilerConfigurationBlock_aj_other_tabtitle;
	public static String CompilerConfigurationBlock_aj_other_description;
	public static String CompilerConfigurationBlock_aj_enable_incremental_label;
	public static String CompilerConfigurationBlock_aj_enable_build_asm_label;
	public static String CompilerConfigurationBlock_aj_enable_weave_messages_label;
	public static String CompilerConfigurationBlock_aj_5_description;
	public static String CompilerConfigurationBlock_aj_5_tabtitle;
	public static String CompilerConfigurationBlock_noJoinpointsForBridgeMethods;
	public static String CompilerConfigurationBlock_cantMatchArrayTypeOnVarargs;
	public static String CompilerConfigurationBlock_enumAsTargetForDecpIgnored;
	public static String CompilerConfigurationBlock_annotationAsTargetForDecpIgnored;
	public static String CompilerConfigurationBlock_invalidTargetForAnnotation;
	public static String CompilerConfigurationBlock_elementAlreadyAnnotated;
	public static String CompilerConfigurationBlock_adviceDidNotMatch;
	public static String CompilerConfigurationBlock_runtimeExceptionNotSoftened;
	public static String InPathBlock_tab_inpath_order;
	public static String InPathBlock_tab_libraries;
	public static String InPathBlock_order_up_button;
	public static String InPathBlock_order_down_button;
	public static String AspectPathBlock_tab_libraries;
	public static String InPathLibrariesWorkbookPage_NewClassFolderDialog_new_title;
	public static String InPathLibrariesWorkbookPage_NewClassFolderDialog_edit_title;
	public static String InPathLibrariesWorkbookPage_NewClassFolderDialog_description;
	public static String InPathLibrariesWorkbookPage_configurecontainer_error_title;
	public static String InPathLibrariesWorkbookPage_configurecontainer_error_message;
	public static String InPathLibrariesWorkbookPage_exclusion_added_title;
	public static String InPathLibrariesWorkbookPage_exclusion_added_message;
	public static String InPathLibrariesWorkbookPage_libraries_inpath_label;
	public static String InPathLibrariesWorkbookPage_libraries_addjar_button;
	public static String InPathLibrariesWorkbookPage_libraries_addextjar_button;
	public static String InPathLibrariesWorkbookPage_libraries_addvariable_button;
	public static String InPathLibrariesWorkbookPage_libraries_addlibrary_button;
	public static String InPathLibrariesWorkbookPage_libraries_addclassfolder_button;
	public static String InPathLibrariesWorkbookPage_libraries_edit_button;
	public static String InPathLibrariesWorkbookPage_libraries_remove_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_edit_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_remove_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_aspectpath_label;
	public static String AspectPathLibrariesWorkbookPage_libraries_addjar_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_addextjar_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_addvariable_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_addlibrary_button;
	public static String AspectPathLibrariesWorkbookPage_libraries_addclassfolder_button;
	public static String AspectPathLibrariesWorkbookPage_NewClassFolderDialog_description;
	public static String AspectPathLibrariesWorkbookPage_configurecontainer_error_title;
	public static String AspectPathLibrariesWorkbookPage_configurecontainer_error_message;
	public static String AspectPathLibrariesWorkbookPage_exclusion_added_message;
	public static String InPathBlock_operationdesc_java;
	public static String InPathBlock_warning_EntryMissing;
	public static String InPathBlock_warning_EntriesMissing;
	public static String InPathBlock_inpath_label;
	public static String InPathBlock_inpath_checkall_button;
	public static String InPathBlock_inpath_uncheckall_button;
	public static String AspectPathBlock_aspectpath_label;
	public static String AspectPathBlock_warning_EntryMissing;
	public static String AspectBlock_warning_EntriesMissing;
	public static String AspectPathBlock_operationdesc_java;
	public static String InPathProp_exceptionInitializingInpath_title;
	public static String InPathProp_exceptionInitializingInpath_message;
	public static String AspectPathProp_exceptionInitializingAspectpath_title;
	public static String AspectPathProp_exceptionInitializingAspectpath_message;
	public static String Path_entryNotFound_warningTitle;
	public static String Path_entryNotFound_warningMessage;
	public static String BuildPage_name;
	public static String AJPropsEditor_BuildPage_title ;
	public static String AJPropsEditor_SrcSection_title ;
	public static String AJPropsEditor_SrcSection_desc ;
	public static String buildConfig_exceptionIncluding;
	public static String buildConfig_exceptionException;
	public static String buildConfig_exceptionWriting ;
	public static String buildConfig_notFound;
	public static String buildConfig_standardFileName;
	public static String BCDialog_Overwrite_title ;
	public static String BCDialog_Overwrite_message ;
	public static String BCDialog_Overwrite_yes ;
	public static String BCDialog_Overwrite_no ;
	public static String BCDialog_SaveLstAsAJProp_title ;
	public static String BCDialog_SaveLstAsAJProp_message ;
	public static String BCDialog_SaveAJPropAsLst_title ;
	public static String BCDialog_SaveAJPropAsLst_message ;
	public static String BCDialog_Activate_check;
	public static String BCDialog_SaveBuildConfigurationAs_title ;
	public static String BCDialog_SaveBuildConfigurationAs_message ;
	public static String BCDialog_SaveBuildConfigurationAs_default;
	public static String BCDialog_NameValidator_ExistsError;
	public static String BCLabels_SaveBCAs;
	public static String BCLabels_ConfigurationSelectionMenu;
	public static String BCLabels_IncludeAction;
	public static String BCLabels_ExcludeAction;
	public static String ajdocWizard_javadocwizard_title;
	public static String ajdocWizard_ajdocprocess_label;
	public static String ajdocWizardPage_javadocwizardpage_description;
	public static String ajdocTreeWizardPage_visibilitygroup_label;
	public static String ajdocTreeWizardPage_privatevisibilitydescription_label;
	public static String ajdocTreeWizardPage_packagevisibledescription_label;
	public static String ajdocTreeWizardPage_protectedvisibilitydescription_label;
	public static String ajdocTreeWizardPage_publicvisibilitydescription_label;
	public static String ajdocTreeWizardPage_javadoctreewizardpage_description;
	public static String ajdocTreeWizardPage_warning_mayoverwritefiles;
	public static String ajdocTreeWizardPage_ajdoccommand_label;
	public static String ajdocTreeWizardPage_ajdoccmd_error_enterpath;
	public static String ajdocTreeWizardPage_ajdoccmd_error_notexists;
	public static String AJdocTreeWizardPage_ajdoccmd_dialog_title;
	public static String ajdocSpecificsWizardPage_description;
	public static String ajdocSpecificsWizardPage_extraoptionsfield_label;
	public static String ajdocSpecificsWizardPage_vmoptionsfield_label;
	public static String ajdoc_error_noProjectSelected;
	public static String ajdoc_info_projectselection;
	public static String ajdocWizard_error_title;
	public static String ajdocWizard_launch_error_message;
	public static String ajdocWizard_exec_error_message;
	public static String breakpoint_missingStructureModel;
	public static String breakpoint_fileExcluded;
	public static String myEclipse_natureDetected_title;
	public static String myEclipse_natureDetected_message;
	public static String AspectJWarning;
	public static String AspectJError;
	public static String ResetColorMemory;
	public static String HideErrors;
	public static String HideWarnings;
	public static String Launcher_aspectPath;
	public static String Launcher_outJar;
	public static String FileFilterDialog_JobTitle;
	public static String FileFilterDialog_Title;
	public static String FileFilterDialog_Message;
	public static String FileFilterDialog_CheckboxCaption;
	public static String codeAssist_limited_title;
	public static String codeAssist_limited_message;
	public static String Refactoring_ErrorRenamingResource;
	public static String Refactoring_ConvertAllToJava;
	public static String Refactoring_ConvertAllToAJ;
	public static String Refactoring_ConvertAspectsToAJAndClassesToJava;
	public static String Refactoring_IncludeFilesNotInBuild;
	public static String Refactoring_UpdateBuildConfigs;
	public static String Refactoring_ConvertFileExtensions;
	public static String Refactoring_ConvertingFileExtensions;
	public static String utils_refresh_explorer_job;
	public static String utils_refresh_outline_job;
	public static String wrong_eclipse_version;
	public static String savemap_dialog_title;
	public static String savemap_dialog_message;
	public static String changesView_table_column1;
	public static String changesView_table_column2;
	public static String changesView_table_column3;
	public static String changesView_table_column4;
	public static String changesView_table_added;
	public static String changesView_table_removed;
	public static String changesView_description;
	public static String changesView_currentBuild;
	public static String ajmapEditor_title;
	public static String ajmapEditor_heading;
	public static String ajmapEditor_description;
	public static String changesView_filter_dialog_title;
	public static String changesView_filter_dialog_message;
	public static String changesView_filter_dialog_showingXofY;
	public static String quickFix_JToAJQuickFixProcessor;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, UIMessages.class);
	}
}
