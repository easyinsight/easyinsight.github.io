
package com.easyinsight.rowutil.transactional;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.easyinsight.rowutil.transactional package. 
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

    private final static QName _ValidateCredentials_QNAME = new QName("http://v2.api.easyinsight.com/", "validateCredentials");
    private final static QName _Commit_QNAME = new QName("http://v2.api.easyinsight.com/", "commit");
    private final static QName _LoadRowsResponse_QNAME = new QName("http://v2.api.easyinsight.com/", "loadRowsResponse");
    private final static QName _LoadRows_QNAME = new QName("http://v2.api.easyinsight.com/", "loadRows");
    private final static QName _ValidateCredentialsResponse_QNAME = new QName("http://v2.api.easyinsight.com/", "validateCredentialsResponse");
    private final static QName _BeginTransactionResponse_QNAME = new QName("http://v2.api.easyinsight.com/", "beginTransactionResponse");
    private final static QName _RollbackResponse_QNAME = new QName("http://v2.api.easyinsight.com/", "rollbackResponse");
    private final static QName _BeginTransaction_QNAME = new QName("http://v2.api.easyinsight.com/", "beginTransaction");
    private final static QName _CommitResponse_QNAME = new QName("http://v2.api.easyinsight.com/", "commitResponse");
    private final static QName _Rollback_QNAME = new QName("http://v2.api.easyinsight.com/", "rollback");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.easyinsight.rowutil.transactional
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BeginTransactionResponse }
     * 
     */
    public BeginTransactionResponse createBeginTransactionResponse() {
        return new BeginTransactionResponse();
    }

    /**
     * Create an instance of {@link CommitResponse }
     * 
     */
    public CommitResponse createCommitResponse() {
        return new CommitResponse();
    }

    /**
     * Create an instance of {@link CommitResult }
     * 
     */
    public CommitResult createCommitResult() {
        return new CommitResult();
    }

    /**
     * Create an instance of {@link DatePair }
     * 
     */
    public DatePair createDatePair() {
        return new DatePair();
    }

    /**
     * Create an instance of {@link ValidateCredentials }
     * 
     */
    public ValidateCredentials createValidateCredentials() {
        return new ValidateCredentials();
    }

    /**
     * Create an instance of {@link Rollback }
     * 
     */
    public Rollback createRollback() {
        return new Rollback();
    }

    /**
     * Create an instance of {@link ValidateCredentialsResponse }
     * 
     */
    public ValidateCredentialsResponse createValidateCredentialsResponse() {
        return new ValidateCredentialsResponse();
    }

    /**
     * Create an instance of {@link Commit }
     * 
     */
    public Commit createCommit() {
        return new Commit();
    }

    /**
     * Create an instance of {@link NumberPair }
     * 
     */
    public NumberPair createNumberPair() {
        return new NumberPair();
    }

    /**
     * Create an instance of {@link BeginTransaction }
     * 
     */
    public BeginTransaction createBeginTransaction() {
        return new BeginTransaction();
    }

    /**
     * Create an instance of {@link StringPair }
     * 
     */
    public StringPair createStringPair() {
        return new StringPair();
    }

    /**
     * Create an instance of {@link RowStatus }
     * 
     */
    public RowStatus createRowStatus() {
        return new RowStatus();
    }

    /**
     * Create an instance of {@link RollbackResponse }
     * 
     */
    public RollbackResponse createRollbackResponse() {
        return new RollbackResponse();
    }

    /**
     * Create an instance of {@link LoadRowsResponse }
     * 
     */
    public LoadRowsResponse createLoadRowsResponse() {
        return new LoadRowsResponse();
    }

    /**
     * Create an instance of {@link Row }
     * 
     */
    public Row createRow() {
        return new Row();
    }

    /**
     * Create an instance of {@link LoadRows }
     * 
     */
    public LoadRows createLoadRows() {
        return new LoadRows();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateCredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "validateCredentials")
    public JAXBElement<ValidateCredentials> createValidateCredentials(ValidateCredentials value) {
        return new JAXBElement<ValidateCredentials>(_ValidateCredentials_QNAME, ValidateCredentials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Commit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "commit")
    public JAXBElement<Commit> createCommit(Commit value) {
        return new JAXBElement<Commit>(_Commit_QNAME, Commit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "loadRowsResponse")
    public JAXBElement<LoadRowsResponse> createLoadRowsResponse(LoadRowsResponse value) {
        return new JAXBElement<LoadRowsResponse>(_LoadRowsResponse_QNAME, LoadRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "loadRows")
    public JAXBElement<LoadRows> createLoadRows(LoadRows value) {
        return new JAXBElement<LoadRows>(_LoadRows_QNAME, LoadRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateCredentialsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "validateCredentialsResponse")
    public JAXBElement<ValidateCredentialsResponse> createValidateCredentialsResponse(ValidateCredentialsResponse value) {
        return new JAXBElement<ValidateCredentialsResponse>(_ValidateCredentialsResponse_QNAME, ValidateCredentialsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "beginTransactionResponse")
    public JAXBElement<BeginTransactionResponse> createBeginTransactionResponse(BeginTransactionResponse value) {
        return new JAXBElement<BeginTransactionResponse>(_BeginTransactionResponse_QNAME, BeginTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RollbackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "rollbackResponse")
    public JAXBElement<RollbackResponse> createRollbackResponse(RollbackResponse value) {
        return new JAXBElement<RollbackResponse>(_RollbackResponse_QNAME, RollbackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "beginTransaction")
    public JAXBElement<BeginTransaction> createBeginTransaction(BeginTransaction value) {
        return new JAXBElement<BeginTransaction>(_BeginTransaction_QNAME, BeginTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "commitResponse")
    public JAXBElement<CommitResponse> createCommitResponse(CommitResponse value) {
        return new JAXBElement<CommitResponse>(_CommitResponse_QNAME, CommitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rollback }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v2.api.easyinsight.com/", name = "rollback")
    public JAXBElement<Rollback> createRollback(Rollback value) {
        return new JAXBElement<Rollback>(_Rollback_QNAME, Rollback.class, null, value);
    }

}
