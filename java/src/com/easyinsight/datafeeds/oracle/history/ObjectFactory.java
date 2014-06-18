
package com.easyinsight.datafeeds.oracle.history;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.easyinsight.datafeeds.oracle.history package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InvalidParametersException_QNAME = new QName("http://xmlns.oracle.com/oxp/service/PublicReportService", "InvalidParametersException");
    private final static QName _SchedulingException_QNAME = new QName("http://xmlns.oracle.com/oxp/service/PublicReportService", "SchedulingException");
    private final static QName _OperationFailedException_QNAME = new QName("http://xmlns.oracle.com/oxp/service/PublicReportService", "OperationFailedException");
    private final static QName _AccessDeniedException_QNAME = new QName("http://xmlns.oracle.com/oxp/service/PublicReportService", "AccessDeniedException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.easyinsight.datafeeds.oracle.history
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeliveryService }
     * 
     */
    public DeliveryService createDeliveryService() {
        return new DeliveryService();
    }

    /**
     * Create an instance of {@link DeliveryRequest }
     * 
     */
    public DeliveryRequest createDeliveryRequest() {
        return new DeliveryRequest();
    }

    /**
     * Create an instance of {@link AccessDeniedException }
     * 
     */
    public AccessDeniedException createAccessDeniedException() {
        return new AccessDeniedException();
    }

    /**
     * Create an instance of {@link UploadTemplateForReportResponse }
     * 
     */
    public UploadTemplateForReportResponse createUploadTemplateForReportResponse() {
        return new UploadTemplateForReportResponse();
    }

    /**
     * Create an instance of {@link UpdateReportDefinitionResponse }
     * 
     */
    public UpdateReportDefinitionResponse createUpdateReportDefinitionResponse() {
        return new UpdateReportDefinitionResponse();
    }

    /**
     * Create an instance of {@link DownloadReportObject }
     * 
     */
    public DownloadReportObject createDownloadReportObject() {
        return new DownloadReportObject();
    }

    /**
     * Create an instance of {@link GetReportDefinitionResponse }
     * 
     */
    public GetReportDefinitionResponse createGetReportDefinitionResponse() {
        return new GetReportDefinitionResponse();
    }

    /**
     * Create an instance of {@link ReportDefinition }
     * 
     */
    public ReportDefinition createReportDefinition() {
        return new ReportDefinition();
    }

    /**
     * Create an instance of {@link GetReportSampleDataResponse }
     * 
     */
    public GetReportSampleDataResponse createGetReportSampleDataResponse() {
        return new GetReportSampleDataResponse();
    }

    /**
     * Create an instance of {@link DownloadReportDataChunk }
     * 
     */
    public DownloadReportDataChunk createDownloadReportDataChunk() {
        return new DownloadReportDataChunk();
    }

    /**
     * Create an instance of {@link GetFolderContents }
     * 
     */
    public GetFolderContents createGetFolderContents() {
        return new GetFolderContents();
    }

    /**
     * Create an instance of {@link GetDeliveryServiceDefinition }
     * 
     */
    public GetDeliveryServiceDefinition createGetDeliveryServiceDefinition() {
        return new GetDeliveryServiceDefinition();
    }

    /**
     * Create an instance of {@link DeleteReport }
     * 
     */
    public DeleteReport createDeleteReport() {
        return new DeleteReport();
    }

    /**
     * Create an instance of {@link DownloadReportDataChunkResponse }
     * 
     */
    public DownloadReportDataChunkResponse createDownloadReportDataChunkResponse() {
        return new DownloadReportDataChunkResponse();
    }

    /**
     * Create an instance of {@link ReportDataChunk }
     * 
     */
    public ReportDataChunk createReportDataChunk() {
        return new ReportDataChunk();
    }

    /**
     * Create an instance of {@link GetDeliveryServiceDefinitionResponse }
     * 
     */
    public GetDeliveryServiceDefinitionResponse createGetDeliveryServiceDefinitionResponse() {
        return new GetDeliveryServiceDefinitionResponse();
    }

    /**
     * Create an instance of {@link DeliveryServiceDefinition }
     * 
     */
    public DeliveryServiceDefinition createDeliveryServiceDefinition() {
        return new DeliveryServiceDefinition();
    }

    /**
     * Create an instance of {@link GetBIPHTTPSessionIntervalResponse }
     * 
     */
    public GetBIPHTTPSessionIntervalResponse createGetBIPHTTPSessionIntervalResponse() {
        return new GetBIPHTTPSessionIntervalResponse();
    }

    /**
     * Create an instance of {@link RunReport }
     * 
     */
    public RunReport createRunReport() {
        return new RunReport();
    }

    /**
     * Create an instance of {@link ReportRequest }
     * 
     */
    public ReportRequest createReportRequest() {
        return new ReportRequest();
    }

    /**
     * Create an instance of {@link SubmitReport }
     * 
     */
    public SubmitReport createSubmitReport() {
        return new SubmitReport();
    }

    /**
     * Create an instance of {@link ValidateLogin }
     * 
     */
    public ValidateLogin createValidateLogin() {
        return new ValidateLogin();
    }

    /**
     * Create an instance of {@link GetBIPHTTPSessionInterval }
     * 
     */
    public GetBIPHTTPSessionInterval createGetBIPHTTPSessionInterval() {
        return new GetBIPHTTPSessionInterval();
    }

    /**
     * Create an instance of {@link GetFolderContentsResponse }
     * 
     */
    public GetFolderContentsResponse createGetFolderContentsResponse() {
        return new GetFolderContentsResponse();
    }

    /**
     * Create an instance of {@link CatalogContents }
     * 
     */
    public CatalogContents createCatalogContents() {
        return new CatalogContents();
    }

    /**
     * Create an instance of {@link GetReportParameters }
     * 
     */
    public GetReportParameters createGetReportParameters() {
        return new GetReportParameters();
    }

    /**
     * Create an instance of {@link GetXDOSchemaResponse }
     * 
     */
    public GetXDOSchemaResponse createGetXDOSchemaResponse() {
        return new GetXDOSchemaResponse();
    }

    /**
     * Create an instance of {@link SubmitReportResponse }
     * 
     */
    public SubmitReportResponse createSubmitReportResponse() {
        return new SubmitReportResponse();
    }

    /**
     * Create an instance of {@link InvalidParametersException }
     * 
     */
    public InvalidParametersException createInvalidParametersException() {
        return new InvalidParametersException();
    }

    /**
     * Create an instance of {@link CreateReportFolderResponse }
     * 
     */
    public CreateReportFolderResponse createCreateReportFolderResponse() {
        return new CreateReportFolderResponse();
    }

    /**
     * Create an instance of {@link DeleteReportResponse }
     * 
     */
    public DeleteReportResponse createDeleteReportResponse() {
        return new DeleteReportResponse();
    }

    /**
     * Create an instance of {@link DeleteFolderResponse }
     * 
     */
    public DeleteFolderResponse createDeleteFolderResponse() {
        return new DeleteFolderResponse();
    }

    /**
     * Create an instance of {@link IsReportExistResponse }
     * 
     */
    public IsReportExistResponse createIsReportExistResponse() {
        return new IsReportExistResponse();
    }

    /**
     * Create an instance of {@link GetTemplateParametersResponse }
     * 
     */
    public GetTemplateParametersResponse createGetTemplateParametersResponse() {
        return new GetTemplateParametersResponse();
    }

    /**
     * Create an instance of {@link ParamNameValue }
     * 
     */
    public ParamNameValue createParamNameValue() {
        return new ParamNameValue();
    }

    /**
     * Create an instance of {@link IsFolderExistResponse }
     * 
     */
    public IsFolderExistResponse createIsFolderExistResponse() {
        return new IsFolderExistResponse();
    }

    /**
     * Create an instance of {@link UpdateReportDefinition }
     * 
     */
    public UpdateReportDefinition createUpdateReportDefinition() {
        return new UpdateReportDefinition();
    }

    /**
     * Create an instance of {@link UpdateTemplateForReport }
     * 
     */
    public UpdateTemplateForReport createUpdateTemplateForReport() {
        return new UpdateTemplateForReport();
    }

    /**
     * Create an instance of {@link UploadReportDataChunkResponse }
     * 
     */
    public UploadReportDataChunkResponse createUploadReportDataChunkResponse() {
        return new UploadReportDataChunkResponse();
    }

    /**
     * Create an instance of {@link IsFolderExist }
     * 
     */
    public IsFolderExist createIsFolderExist() {
        return new IsFolderExist();
    }

    /**
     * Create an instance of {@link GetSecurityModelResponse }
     * 
     */
    public GetSecurityModelResponse createGetSecurityModelResponse() {
        return new GetSecurityModelResponse();
    }

    /**
     * Create an instance of {@link CreateReportResponse }
     * 
     */
    public CreateReportResponse createCreateReportResponse() {
        return new CreateReportResponse();
    }

    /**
     * Create an instance of {@link DeleteFolder }
     * 
     */
    public DeleteFolder createDeleteFolder() {
        return new DeleteFolder();
    }

    /**
     * Create an instance of {@link UploadReportObjectResponse }
     * 
     */
    public UploadReportObjectResponse createUploadReportObjectResponse() {
        return new UploadReportObjectResponse();
    }

    /**
     * Create an instance of {@link GetReportParametersResponse }
     * 
     */
    public GetReportParametersResponse createGetReportParametersResponse() {
        return new GetReportParametersResponse();
    }

    /**
     * Create an instance of {@link ParamNameValues }
     * 
     */
    public ParamNameValues createParamNameValues() {
        return new ParamNameValues();
    }

    /**
     * Create an instance of {@link UpdateTemplateForReportResponse }
     * 
     */
    public UpdateTemplateForReportResponse createUpdateTemplateForReportResponse() {
        return new UpdateTemplateForReportResponse();
    }

    /**
     * Create an instance of {@link RemoveTemplateForReportResponse }
     * 
     */
    public RemoveTemplateForReportResponse createRemoveTemplateForReportResponse() {
        return new RemoveTemplateForReportResponse();
    }

    /**
     * Create an instance of {@link ValidateLoginResponse }
     * 
     */
    public ValidateLoginResponse createValidateLoginResponse() {
        return new ValidateLoginResponse();
    }

    /**
     * Create an instance of {@link IsReportExist }
     * 
     */
    public IsReportExist createIsReportExist() {
        return new IsReportExist();
    }

    /**
     * Create an instance of {@link GetReportDefinition }
     * 
     */
    public GetReportDefinition createGetReportDefinition() {
        return new GetReportDefinition();
    }

    /**
     * Create an instance of {@link CreateReport }
     * 
     */
    public CreateReport createCreateReport() {
        return new CreateReport();
    }

    /**
     * Create an instance of {@link GetTemplateParameters }
     * 
     */
    public GetTemplateParameters createGetTemplateParameters() {
        return new GetTemplateParameters();
    }

    /**
     * Create an instance of {@link DeliveryServiceResponse }
     * 
     */
    public DeliveryServiceResponse createDeliveryServiceResponse() {
        return new DeliveryServiceResponse();
    }

    /**
     * Create an instance of {@link CreateReportFolder }
     * 
     */
    public CreateReportFolder createCreateReportFolder() {
        return new CreateReportFolder();
    }

    /**
     * Create an instance of {@link GetTemplateResponse }
     * 
     */
    public GetTemplateResponse createGetTemplateResponse() {
        return new GetTemplateResponse();
    }

    /**
     * Create an instance of {@link RemoveTemplateForReport }
     * 
     */
    public RemoveTemplateForReport createRemoveTemplateForReport() {
        return new RemoveTemplateForReport();
    }

    /**
     * Create an instance of {@link UploadReportDataChunk }
     * 
     */
    public UploadReportDataChunk createUploadReportDataChunk() {
        return new UploadReportDataChunk();
    }

    /**
     * Create an instance of {@link GetTemplate }
     * 
     */
    public GetTemplate createGetTemplate() {
        return new GetTemplate();
    }

    /**
     * Create an instance of {@link OperationFailedException }
     * 
     */
    public OperationFailedException createOperationFailedException() {
        return new OperationFailedException();
    }

    /**
     * Create an instance of {@link UploadTemplateForReport }
     * 
     */
    public UploadTemplateForReport createUploadTemplateForReport() {
        return new UploadTemplateForReport();
    }

    /**
     * Create an instance of {@link DownloadReportObjectResponse }
     * 
     */
    public DownloadReportObjectResponse createDownloadReportObjectResponse() {
        return new DownloadReportObjectResponse();
    }

    /**
     * Create an instance of {@link GetXDOSchema }
     * 
     */
    public GetXDOSchema createGetXDOSchema() {
        return new GetXDOSchema();
    }

    /**
     * Create an instance of {@link HasReportAccess }
     * 
     */
    public HasReportAccess createHasReportAccess() {
        return new HasReportAccess();
    }

    /**
     * Create an instance of {@link GetSecurityModel }
     * 
     */
    public GetSecurityModel createGetSecurityModel() {
        return new GetSecurityModel();
    }

    /**
     * Create an instance of {@link SchedulingException }
     * 
     */
    public SchedulingException createSchedulingException() {
        return new SchedulingException();
    }

    /**
     * Create an instance of {@link GetReportSampleData }
     * 
     */
    public GetReportSampleData createGetReportSampleData() {
        return new GetReportSampleData();
    }

    /**
     * Create an instance of {@link HasReportAccessResponse }
     * 
     */
    public HasReportAccessResponse createHasReportAccessResponse() {
        return new HasReportAccessResponse();
    }

    /**
     * Create an instance of {@link UploadReportObject }
     * 
     */
    public UploadReportObject createUploadReportObject() {
        return new UploadReportObject();
    }

    /**
     * Create an instance of {@link RunReportResponse }
     * 
     */
    public RunReportResponse createRunReportResponse() {
        return new RunReportResponse();
    }

    /**
     * Create an instance of {@link ReportResponse }
     * 
     */
    public ReportResponse createReportResponse() {
        return new ReportResponse();
    }

    /**
     * Create an instance of {@link PrintDeliveryOption }
     * 
     */
    public PrintDeliveryOption createPrintDeliveryOption() {
        return new PrintDeliveryOption();
    }

    /**
     * Create an instance of {@link TemplateFormatsLabelValues }
     * 
     */
    public TemplateFormatsLabelValues createTemplateFormatsLabelValues() {
        return new TemplateFormatsLabelValues();
    }

    /**
     * Create an instance of {@link WebDAVDeliveryOption }
     * 
     */
    public WebDAVDeliveryOption createWebDAVDeliveryOption() {
        return new WebDAVDeliveryOption();
    }

    /**
     * Create an instance of {@link ArrayOfTemplateFormatsLabelValues }
     * 
     */
    public ArrayOfTemplateFormatsLabelValues createArrayOfTemplateFormatsLabelValues() {
        return new ArrayOfTemplateFormatsLabelValues();
    }

    /**
     * Create an instance of {@link ArrayOfItemData }
     * 
     */
    public ArrayOfItemData createArrayOfItemData() {
        return new ArrayOfItemData();
    }

    /**
     * Create an instance of {@link EMailDeliveryOption }
     * 
     */
    public EMailDeliveryOption createEMailDeliveryOption() {
        return new EMailDeliveryOption();
    }

    /**
     * Create an instance of {@link TemplateFormatLabelValue }
     * 
     */
    public TemplateFormatLabelValue createTemplateFormatLabelValue() {
        return new TemplateFormatLabelValue();
    }

    /**
     * Create an instance of {@link ArrayOfParamNameValue }
     * 
     */
    public ArrayOfParamNameValue createArrayOfParamNameValue() {
        return new ArrayOfParamNameValue();
    }

    /**
     * Create an instance of {@link MetaData }
     * 
     */
    public MetaData createMetaData() {
        return new MetaData();
    }

    /**
     * Create an instance of {@link ArrayOfTemplateFormatLabelValue }
     * 
     */
    public ArrayOfTemplateFormatLabelValue createArrayOfTemplateFormatLabelValue() {
        return new ArrayOfTemplateFormatLabelValue();
    }

    /**
     * Create an instance of {@link FileDataSource }
     * 
     */
    public FileDataSource createFileDataSource() {
        return new FileDataSource();
    }

    /**
     * Create an instance of {@link FaxDeliveryOption }
     * 
     */
    public FaxDeliveryOption createFaxDeliveryOption() {
        return new FaxDeliveryOption();
    }

    /**
     * Create an instance of {@link LocalDeliveryOption }
     * 
     */
    public LocalDeliveryOption createLocalDeliveryOption() {
        return new LocalDeliveryOption();
    }

    /**
     * Create an instance of {@link MetaDataList }
     * 
     */
    public MetaDataList createMetaDataList() {
        return new MetaDataList();
    }

    /**
     * Create an instance of {@link FTPDeliveryOption }
     * 
     */
    public FTPDeliveryOption createFTPDeliveryOption() {
        return new FTPDeliveryOption();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link JDBCDataSource }
     * 
     */
    public JDBCDataSource createJDBCDataSource() {
        return new JDBCDataSource();
    }

    /**
     * Create an instance of {@link BIPDataSource }
     * 
     */
    public BIPDataSource createBIPDataSource() {
        return new BIPDataSource();
    }

    /**
     * Create an instance of {@link ItemData }
     * 
     */
    public ItemData createItemData() {
        return new ItemData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidParametersException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oxp/service/PublicReportService", name = "InvalidParametersException")
    public JAXBElement<InvalidParametersException> createInvalidParametersException(InvalidParametersException value) {
        return new JAXBElement<InvalidParametersException>(_InvalidParametersException_QNAME, InvalidParametersException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SchedulingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oxp/service/PublicReportService", name = "SchedulingException")
    public JAXBElement<SchedulingException> createSchedulingException(SchedulingException value) {
        return new JAXBElement<SchedulingException>(_SchedulingException_QNAME, SchedulingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationFailedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oxp/service/PublicReportService", name = "OperationFailedException")
    public JAXBElement<OperationFailedException> createOperationFailedException(OperationFailedException value) {
        return new JAXBElement<OperationFailedException>(_OperationFailedException_QNAME, OperationFailedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccessDeniedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oxp/service/PublicReportService", name = "AccessDeniedException")
    public JAXBElement<AccessDeniedException> createAccessDeniedException(AccessDeniedException value) {
        return new JAXBElement<AccessDeniedException>(_AccessDeniedException_QNAME, AccessDeniedException.class, null, value);
    }

}
