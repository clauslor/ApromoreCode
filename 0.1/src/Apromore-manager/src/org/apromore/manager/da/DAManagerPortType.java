package org.apromore.manager.da;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.7
 * Tue Jun 29 15:56:32 EST 2010
 * Generated source version: 2.2.7
 * 
 */
 
@WebService(targetNamespace = "http://www.apromore.org/data_access/service_manager", name = "DAManagerPortType")
@XmlSeeAlso({org.apromore.manager.model_da.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DAManagerPortType {

    @WebResult(name = "DeleteProcessVersionsOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "DeleteProcessVersions")
    public org.apromore.manager.model_da.DeleteProcessVersionsOutputMsgType deleteProcessVersions(
        @WebParam(partName = "payload", name = "DeleteProcessVersionsInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.DeleteProcessVersionsInputMsgType payload
    );

    @WebResult(name = "WriteUserOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "WriteUser")
    public org.apromore.manager.model_da.WriteUserOutputMsgType writeUser(
        @WebParam(partName = "payload", name = "WriteUserInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.WriteUserInputMsgType payload
    );

    @WebResult(name = "ReadNativeOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadNative")
    public org.apromore.manager.model_da.ReadNativeOutputMsgType readNative(
        @WebParam(partName = "payload", name = "ReadNativeInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadNativeInputMsgType payload
    );

    @WebResult(name = "DeleteEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "DeleteEditSession")
    public org.apromore.manager.model_da.DeleteEditSessionOutputMsgType deleteEditSession(
        @WebParam(partName = "payload", name = "DeleteEditSessionInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.DeleteEditSessionInputMsgType payload
    );

    @WebResult(name = "ReadEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadEditSession")
    public org.apromore.manager.model_da.ReadEditSessionOutputMsgType readEditSession(
        @WebParam(partName = "payload", name = "ReadEditSessionInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadEditSessionInputMsgType payload
    );

    @WebResult(name = "ReadDomainsOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadDomains")
    public org.apromore.manager.model_da.ReadDomainsOutputMsgType readDomains(
        @WebParam(partName = "payload", name = "ReadDomainsInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadDomainsInputMsgType payload
    );

    @WebResult(name = "WriteEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "WriteEditSession")
    public org.apromore.manager.model_da.WriteEditSessionOutputMsgType writeEditSession(
        @WebParam(partName = "payload", name = "WriteEditSessionInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.WriteEditSessionInputMsgType payload
    );

    @WebResult(name = "ReadFormatsOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadFormats")
    public org.apromore.manager.model_da.ReadFormatsOutputMsgType readFormats(
        @WebParam(partName = "payload", name = "ReadFormatsInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadFormatsInputMsgType payload
    );

    @WebResult(name = "ReadCanonicalAnfOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadCanonicalAnf")
    public org.apromore.manager.model_da.ReadCanonicalAnfOutputMsgType readCanonicalAnf(
        @WebParam(partName = "payload", name = "ReadCanonicalAnfInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadCanonicalAnfInputMsgType payload
    );

    @WebResult(name = "ReadUserOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadUser")
    public org.apromore.manager.model_da.ReadUserOutputMsgType readUser(
        @WebParam(partName = "payload", name = "ReadUserInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadUserInputMsgType payload
    );

    @WebResult(name = "ReadProcessSummariesOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager", partName = "payload")
    @WebMethod(operationName = "ReadProcessSummaries")
    public org.apromore.manager.model_da.ReadProcessSummariesOutputMsgType readProcessSummaries(
        @WebParam(partName = "payload", name = "ReadProcessSummariesInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_manager")
        org.apromore.manager.model_da.ReadProcessSummariesInputMsgType payload
    );
}
