
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.easyinsight.rowutil.v3web package. 
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

    private final static QName _Rollback_QNAME = new QName("http://v3.api.easyinsight.com/", "rollback");
    private final static QName _DefineDataSourceResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "defineDataSourceResponse");
    private final static QName _GetSourceInfo_QNAME = new QName("http://v3.api.easyinsight.com/", "getSourceInfo");
    private final static QName _BeginTransaction_QNAME = new QName("http://v3.api.easyinsight.com/", "beginTransaction");
    private final static QName _LoadRows_QNAME = new QName("http://v3.api.easyinsight.com/", "loadRows");
    private final static QName _DefineCompositeDataSource_QNAME = new QName("http://v3.api.easyinsight.com/", "defineCompositeDataSource");
    private final static QName _BeginTransactionResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "beginTransactionResponse");
    private final static QName _UpdateRowsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "updateRowsResponse");
    private final static QName _UpdateRow_QNAME = new QName("http://v3.api.easyinsight.com/", "updateRow");
    private final static QName _UpdateRows_QNAME = new QName("http://v3.api.easyinsight.com/", "updateRows");
    private final static QName _AddRow_QNAME = new QName("http://v3.api.easyinsight.com/", "addRow");
    private final static QName _RollbackResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "rollbackResponse");
    private final static QName _AddRowsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "addRowsResponse");
    private final static QName _DefineCompositeDataSourceResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "defineCompositeDataSourceResponse");
    private final static QName _Commit_QNAME = new QName("http://v3.api.easyinsight.com/", "commit");
    private final static QName _AddRows_QNAME = new QName("http://v3.api.easyinsight.com/", "addRows");
    private final static QName _DeleteRows_QNAME = new QName("http://v3.api.easyinsight.com/", "deleteRows");
    private final static QName _DeleteRowsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "deleteRowsResponse");
    private final static QName _CommitResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "commitResponse");
    private final static QName _DefineDataSource_QNAME = new QName("http://v3.api.easyinsight.com/", "defineDataSource");
    private final static QName _AddRowResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "addRowResponse");
    private final static QName _ValidateCredentials_QNAME = new QName("http://v3.api.easyinsight.com/", "validateCredentials");
    private final static QName _GetSourceInfoResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "getSourceInfoResponse");
    private final static QName _ReplaceRows_QNAME = new QName("http://v3.api.easyinsight.com/", "replaceRows");
    private final static QName _ReplaceRowsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "replaceRowsResponse");
    private final static QName _ValidateCredentialsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "validateCredentialsResponse");
    private final static QName _UpdateRowResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "updateRowResponse");
    private final static QName _LoadRowsResponse_QNAME = new QName("http://v3.api.easyinsight.com/", "loadRowsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.easyinsight.rowutil.v3web
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Commit }
     * 
     */
    public Commit createCommit() {
        return new Commit();
    }

    /**
     * Create an instance of {@link GetSourceInfoResponse }
     * 
     */
    public GetSourceInfoResponse createGetSourceInfoResponse() {
        return new GetSourceInfoResponse();
    }

    /**
     * Create an instance of {@link CommitResponse }
     * 
     */
    public CommitResponse createCommitResponse() {
        return new CommitResponse();
    }

    /**
     * Create an instance of {@link AddRowsResponse }
     * 
     */
    public AddRowsResponse createAddRowsResponse() {
        return new AddRowsResponse();
    }

    /**
     * Create an instance of {@link DefineDataSource }
     * 
     */
    public DefineDataSource createDefineDataSource() {
        return new DefineDataSource();
    }

    /**
     * Create an instance of {@link AddRow }
     * 
     */
    public AddRow createAddRow() {
        return new AddRow();
    }

    /**
     * Create an instance of {@link LoadRowsResponse }
     * 
     */
    public LoadRowsResponse createLoadRowsResponse() {
        return new LoadRowsResponse();
    }

    /**
     * Create an instance of {@link DeleteRowsResponse }
     * 
     */
    public DeleteRowsResponse createDeleteRowsResponse() {
        return new DeleteRowsResponse();
    }

    /**
     * Create an instance of {@link UpdateRowResponse }
     * 
     */
    public UpdateRowResponse createUpdateRowResponse() {
        return new UpdateRowResponse();
    }

    /**
     * Create an instance of {@link UpdateRows }
     * 
     */
    public UpdateRows createUpdateRows() {
        return new UpdateRows();
    }

    /**
     * Create an instance of {@link Row }
     * 
     */
    public Row createRow() {
        return new Row();
    }

    /**
     * Create an instance of {@link StringPair }
     * 
     */
    public StringPair createStringPair() {
        return new StringPair();
    }

    /**
     * Create an instance of {@link BeginTransaction }
     * 
     */
    public BeginTransaction createBeginTransaction() {
        return new BeginTransaction();
    }

    /**
     * Create an instance of {@link Rollback }
     * 
     */
    public Rollback createRollback() {
        return new Rollback();
    }

    /**
     * Create an instance of {@link ValidateCredentials }
     * 
     */
    public ValidateCredentials createValidateCredentials() {
        return new ValidateCredentials();
    }

    /**
     * Create an instance of {@link DeleteRows }
     * 
     */
    public DeleteRows createDeleteRows() {
        return new DeleteRows();
    }

    /**
     * Create an instance of {@link NumberWhere }
     * 
     */
    public NumberWhere createNumberWhere() {
        return new NumberWhere();
    }

    /**
     * Create an instance of {@link ReplaceRows }
     * 
     */
    public ReplaceRows createReplaceRows() {
        return new ReplaceRows();
    }

    /**
     * Create an instance of {@link AddRows }
     * 
     */
    public AddRows createAddRows() {
        return new AddRows();
    }

    /**
     * Create an instance of {@link UpdateRowsResponse }
     * 
     */
    public UpdateRowsResponse createUpdateRowsResponse() {
        return new UpdateRowsResponse();
    }

    /**
     * Create an instance of {@link DefineCompositeDataSourceResponse }
     * 
     */
    public DefineCompositeDataSourceResponse createDefineCompositeDataSourceResponse() {
        return new DefineCompositeDataSourceResponse();
    }

    /**
     * Create an instance of {@link DataSourceConnection }
     * 
     */
    public DataSourceConnection createDataSourceConnection() {
        return new DataSourceConnection();
    }

    /**
     * Create an instance of {@link AddRowResponse }
     * 
     */
    public AddRowResponse createAddRowResponse() {
        return new AddRowResponse();
    }

    /**
     * Create an instance of {@link RowStatus }
     * 
     */
    public RowStatus createRowStatus() {
        return new RowStatus();
    }

    /**
     * Create an instance of {@link StringWhere }
     * 
     */
    public StringWhere createStringWhere() {
        return new StringWhere();
    }

    /**
     * Create an instance of {@link FieldDefinition }
     * 
     */
    public FieldDefinition createFieldDefinition() {
        return new FieldDefinition();
    }

    /**
     * Create an instance of {@link DefineCompositeDataSource }
     * 
     */
    public DefineCompositeDataSource createDefineCompositeDataSource() {
        return new DefineCompositeDataSource();
    }

    /**
     * Create an instance of {@link DateWhere }
     * 
     */
    public DateWhere createDateWhere() {
        return new DateWhere();
    }

    /**
     * Create an instance of {@link LoadRows }
     * 
     */
    public LoadRows createLoadRows() {
        return new LoadRows();
    }

    /**
     * Create an instance of {@link Where }
     * 
     */
    public Where createWhere() {
        return new Where();
    }

    /**
     * Create an instance of {@link ReplaceRowsResponse }
     * 
     */
    public ReplaceRowsResponse createReplaceRowsResponse() {
        return new ReplaceRowsResponse();
    }

    /**
     * Create an instance of {@link GetSourceInfo }
     * 
     */
    public GetSourceInfo createGetSourceInfo() {
        return new GetSourceInfo();
    }

    /**
     * Create an instance of {@link RollbackResponse }
     * 
     */
    public RollbackResponse createRollbackResponse() {
        return new RollbackResponse();
    }

    /**
     * Create an instance of {@link DayWhere }
     * 
     */
    public DayWhere createDayWhere() {
        return new DayWhere();
    }

    /**
     * Create an instance of {@link DefineDataSourceResponse }
     * 
     */
    public DefineDataSourceResponse createDefineDataSourceResponse() {
        return new DefineDataSourceResponse();
    }

    /**
     * Create an instance of {@link DatePair }
     * 
     */
    public DatePair createDatePair() {
        return new DatePair();
    }

    /**
     * Create an instance of {@link ValidateCredentialsResponse }
     * 
     */
    public ValidateCredentialsResponse createValidateCredentialsResponse() {
        return new ValidateCredentialsResponse();
    }

    /**
     * Create an instance of {@link NumberPair }
     * 
     */
    public NumberPair createNumberPair() {
        return new NumberPair();
    }

    /**
     * Create an instance of {@link CommitResult }
     * 
     */
    public CommitResult createCommitResult() {
        return new CommitResult();
    }

    /**
     * Create an instance of {@link UpdateRow }
     * 
     */
    public UpdateRow createUpdateRow() {
        return new UpdateRow();
    }

    /**
     * Create an instance of {@link BeginTransactionResponse }
     * 
     */
    public BeginTransactionResponse createBeginTransactionResponse() {
        return new BeginTransactionResponse();
    }

    /**
     * Create an instance of {@link DataSourceInfo }
     * 
     */
    public DataSourceInfo createDataSourceInfo() {
        return new DataSourceInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rollback }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "rollback")
    public JAXBElement<Rollback> createRollback(Rollback value) {
        return new JAXBElement<Rollback>(_Rollback_QNAME, Rollback.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineDataSourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "defineDataSourceResponse")
    public JAXBElement<DefineDataSourceResponse> createDefineDataSourceResponse(DefineDataSourceResponse value) {
        return new JAXBElement<DefineDataSourceResponse>(_DefineDataSourceResponse_QNAME, DefineDataSourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSourceInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "getSourceInfo")
    public JAXBElement<GetSourceInfo> createGetSourceInfo(GetSourceInfo value) {
        return new JAXBElement<GetSourceInfo>(_GetSourceInfo_QNAME, GetSourceInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "beginTransaction")
    public JAXBElement<BeginTransaction> createBeginTransaction(BeginTransaction value) {
        return new JAXBElement<BeginTransaction>(_BeginTransaction_QNAME, BeginTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "loadRows")
    public JAXBElement<LoadRows> createLoadRows(LoadRows value) {
        return new JAXBElement<LoadRows>(_LoadRows_QNAME, LoadRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineCompositeDataSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "defineCompositeDataSource")
    public JAXBElement<DefineCompositeDataSource> createDefineCompositeDataSource(DefineCompositeDataSource value) {
        return new JAXBElement<DefineCompositeDataSource>(_DefineCompositeDataSource_QNAME, DefineCompositeDataSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "beginTransactionResponse")
    public JAXBElement<BeginTransactionResponse> createBeginTransactionResponse(BeginTransactionResponse value) {
        return new JAXBElement<BeginTransactionResponse>(_BeginTransactionResponse_QNAME, BeginTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "updateRowsResponse")
    public JAXBElement<UpdateRowsResponse> createUpdateRowsResponse(UpdateRowsResponse value) {
        return new JAXBElement<UpdateRowsResponse>(_UpdateRowsResponse_QNAME, UpdateRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "updateRow")
    public JAXBElement<UpdateRow> createUpdateRow(UpdateRow value) {
        return new JAXBElement<UpdateRow>(_UpdateRow_QNAME, UpdateRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "updateRows")
    public JAXBElement<UpdateRows> createUpdateRows(UpdateRows value) {
        return new JAXBElement<UpdateRows>(_UpdateRows_QNAME, UpdateRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "addRow")
    public JAXBElement<AddRow> createAddRow(AddRow value) {
        return new JAXBElement<AddRow>(_AddRow_QNAME, AddRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RollbackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "rollbackResponse")
    public JAXBElement<RollbackResponse> createRollbackResponse(RollbackResponse value) {
        return new JAXBElement<RollbackResponse>(_RollbackResponse_QNAME, RollbackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "addRowsResponse")
    public JAXBElement<AddRowsResponse> createAddRowsResponse(AddRowsResponse value) {
        return new JAXBElement<AddRowsResponse>(_AddRowsResponse_QNAME, AddRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineCompositeDataSourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "defineCompositeDataSourceResponse")
    public JAXBElement<DefineCompositeDataSourceResponse> createDefineCompositeDataSourceResponse(DefineCompositeDataSourceResponse value) {
        return new JAXBElement<DefineCompositeDataSourceResponse>(_DefineCompositeDataSourceResponse_QNAME, DefineCompositeDataSourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Commit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "commit")
    public JAXBElement<Commit> createCommit(Commit value) {
        return new JAXBElement<Commit>(_Commit_QNAME, Commit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "addRows")
    public JAXBElement<AddRows> createAddRows(AddRows value) {
        return new JAXBElement<AddRows>(_AddRows_QNAME, AddRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "deleteRows")
    public JAXBElement<DeleteRows> createDeleteRows(DeleteRows value) {
        return new JAXBElement<DeleteRows>(_DeleteRows_QNAME, DeleteRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "deleteRowsResponse")
    public JAXBElement<DeleteRowsResponse> createDeleteRowsResponse(DeleteRowsResponse value) {
        return new JAXBElement<DeleteRowsResponse>(_DeleteRowsResponse_QNAME, DeleteRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "commitResponse")
    public JAXBElement<CommitResponse> createCommitResponse(CommitResponse value) {
        return new JAXBElement<CommitResponse>(_CommitResponse_QNAME, CommitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineDataSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "defineDataSource")
    public JAXBElement<DefineDataSource> createDefineDataSource(DefineDataSource value) {
        return new JAXBElement<DefineDataSource>(_DefineDataSource_QNAME, DefineDataSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "addRowResponse")
    public JAXBElement<AddRowResponse> createAddRowResponse(AddRowResponse value) {
        return new JAXBElement<AddRowResponse>(_AddRowResponse_QNAME, AddRowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateCredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "validateCredentials")
    public JAXBElement<ValidateCredentials> createValidateCredentials(ValidateCredentials value) {
        return new JAXBElement<ValidateCredentials>(_ValidateCredentials_QNAME, ValidateCredentials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSourceInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "getSourceInfoResponse")
    public JAXBElement<GetSourceInfoResponse> createGetSourceInfoResponse(GetSourceInfoResponse value) {
        return new JAXBElement<GetSourceInfoResponse>(_GetSourceInfoResponse_QNAME, GetSourceInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReplaceRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "replaceRows")
    public JAXBElement<ReplaceRows> createReplaceRows(ReplaceRows value) {
        return new JAXBElement<ReplaceRows>(_ReplaceRows_QNAME, ReplaceRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReplaceRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "replaceRowsResponse")
    public JAXBElement<ReplaceRowsResponse> createReplaceRowsResponse(ReplaceRowsResponse value) {
        return new JAXBElement<ReplaceRowsResponse>(_ReplaceRowsResponse_QNAME, ReplaceRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateCredentialsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "validateCredentialsResponse")
    public JAXBElement<ValidateCredentialsResponse> createValidateCredentialsResponse(ValidateCredentialsResponse value) {
        return new JAXBElement<ValidateCredentialsResponse>(_ValidateCredentialsResponse_QNAME, ValidateCredentialsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "updateRowResponse")
    public JAXBElement<UpdateRowResponse> createUpdateRowResponse(UpdateRowResponse value) {
        return new JAXBElement<UpdateRowResponse>(_UpdateRowResponse_QNAME, UpdateRowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v3.api.easyinsight.com/", name = "loadRowsResponse")
    public JAXBElement<LoadRowsResponse> createLoadRowsResponse(LoadRowsResponse value) {
        return new JAXBElement<LoadRowsResponse>(_LoadRowsResponse_QNAME, LoadRowsResponse.class, null, value);
    }

}
