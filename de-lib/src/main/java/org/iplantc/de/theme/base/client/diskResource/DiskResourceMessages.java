package org.iplantc.de.theme.base.client.diskResource;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

import java.util.List;

/**
 * @author jstroot
 */
public interface DiskResourceMessages extends Messages {

    String browse();

    String collapseAll();

    String createDataLinksError();

    String createFolderFailed();

    String createIn(String path);

    SafeHtml dataDragDropStatusText(int size);

    String deleteDataLinksError();

    String deleteFailed();

    String deleteMsg();

    String deleteTrash();

    String diskResourceDoesNotExist(String diskResourcePath);

    String diskResourceIncompleteMove();

    String diskResourceMoveSuccess();

    String duplicateCheckFailed();

    String emptyTrash();

    String emptyTrashWarning();

    @Key("fileFolderDialogHeaderText")
    String fileFolderDialogHeaderText();

    @Key("fileFolderSelectorFieldEmptyText")
    String fileFolderSelectorFieldEmptyText();

    String fileName();

    @Key("fileSelectDialogHeaderText")
    String fileSelectDialogHeaderText();

    String fileUploadMaxSizeWarning();

    String folderName();

    String idParentInvalid();

    String importLabel();

    String listDataLinksError();

    String metadataSuccess();

    String metadataUpdateFailed();

    String moveFailed();

    String newFolder();

    String nonDefaultFolderWarning();

    String partialRestore();

    String permissionErrorMessage();

    String permissionSelectErrorMessage();

    String permissions();

    String renameFailed();

    String requiredField();

    String reset();

    String restoreDefaultMsg();

    String restoreMsg();

    String selectAFile();

    String selectAFolder();

    String selectMultipleInputs();

    @Key("selectedFile")
    String selectedFile();

    @Key("selectedFolder")
    String selectedFolder();

    @Key("folderSelectDialogHeaderText")
    String folderSelectDialogHeaderText();

    @Key("selectedItem")
    String selectedItem();

    @Key("share")
    String share();

    @Key("size")
    String size();

    String tabFileConfigDialogCommaRadioLabel();

    String tabFileConfigDialogHeading();

    String tabFileConfigDialogTabRadioLabel();

    @Key("unsupportedCogeInfoType")
    String unsupportedCogeInfoType();

    @Key("unsupportedEnsemblInfoType")
    String unsupportedEnsemblInfoType();

    String uploadingToFolder(String path);

    String urlImport();

    String urlPrompt();

    String projectName();

    String numberOfBioSamples();

    String numberOfLib();

    String ncbiSraProject();

    String ncbiCreateFolderStructureSuccess();

    /**
     * Genome Import Dialog strings
     */
    String heading();

    String loading();

    String importText();

    String searchGenome();

    String organismName();

    String version();

    String chromosomeCount();

    String sequenceType();

    String cogeSearchError();

    String cogeImportGenomeError();

    String cogeImportGenomeSuccess();

    String importFromCoge();

    String bulkMetadataHeading();

    String selectMetadataFile();

    String selectTemplate();

    String applyBulkMetadata();

    String uploadMetadata();

    String templatesError();

    String bulkMetadataSuccess();

    String bulkMetadataError();

    String overWiteMetadata();

    String noRecords();

    String doiRequestFail();

    String doiRequestSuccess();

    String fileUploadsSuccess(@PluralCount List<String> files);

    String inputLbl();

    String folderPathOnlyLbl();

    String selectorEmptyText();

    String patternMatchLbl();

    String infoTypeLbl();

    String destLbl();

    String patternMatchEmptyText();

    String dialogHTHeading();

    String processing();

    String requestSuccess();

    String requestFailed();

    String folderPathOnlyPrompt();

    String validationMessage();

    String select();

    String shareCollab();

    SafeHtml fileSizeViolation(String filename);

    SafeHtml maxFileSizeExceed();

    SafeHtml fileExistTitle();

    String invalidFileName();

    String searchGenomeLabel();

    String dialogMultiInputHeading();
}
