package org.apromore.manager.service_portal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.7
 * Tue Jun 29 16:07:37 EST 2010
 * Generated source version: 2.2.7
 * 
 */
 
@WebService(targetNamespace = "http://www.apromore.org/manager/service_portal", name = "ManagerPortalPortType")
@XmlSeeAlso({org.apromore.manager.model_portal.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ManagerPortalPortType {

    @WebResult(name = "ExportNativeOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ExportNative")
    public org.apromore.manager.model_portal.ExportNativeOutputMsgType exportNative(
        @WebParam(partName = "payload", name = "ExportNativeInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ExportNativeInputMsgType payload
    );

	@WebResult(name = "WriteUserOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "WriteUser")
    public org.apromore.manager.model_portal.WriteUserOutputMsgType writeUser(
        @WebParam(partName = "payload", name = "WriteUserInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.WriteUserInputMsgType payload
    );

    @WebResult(name = "WriteEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "WriteEditSession")
    public org.apromore.manager.model_portal.WriteEditSessionOutputMsgType writeEditSession(
        @WebParam(partName = "payload", name = "WriteEditSessionInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.WriteEditSessionInputMsgType payload
    );

	@WebResult(name = "ReadFormatsOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ReadFormats")
    public org.apromore.manager.model_portal.ReadFormatsOutputMsgType readFormats(
        @WebParam(partName = "payload", name = "ReadFormatsInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ReadFormatsInputMsgType payload
    );

    @WebResult(name = "ReadDomainsOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ReadDomains")
    public org.apromore.manager.model_portal.ReadDomainsOutputMsgType readDomains(
        @WebParam(partName = "payload", name = "ReadDomainsInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ReadDomainsInputMsgType payload
    );

    @WebResult(name = "ReadUserOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ReadUser")
    public org.apromore.manager.model_portal.ReadUserOutputMsgType readUser(
        @WebParam(partName = "payload", name = "ReadUserInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ReadUserInputMsgType payload
    );

    @WebResult(name = "DeleteEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "DeleteEditSession")
    public org.apromore.manager.model_portal.DeleteEditSessionOutputMsgType deleteEditSession(
        @WebParam(partName = "payload", name = "DeleteEditSessionInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.DeleteEditSessionInputMsgType payload
    );

	@WebResult(name = "DeleteProcessVersionsOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "DeleteProcessVersions")
    public org.apromore.manager.model_portal.DeleteProcessVersionsOutputMsgType deleteProcessVersions(
        @WebParam(partName = "payload", name = "DeleteProcessVersionsInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.DeleteProcessVersionsInputMsgType payload
    );

	@WebResult(name = "ImportProcessOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ImportProcess")
    public org.apromore.manager.model_portal.ImportProcessOutputMsgType importProcess(
        @WebParam(partName = "payload", name = "ImportProcessInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ImportProcessInputMsgType payload
    );

    @WebResult(name = "ReadProcessSummariesOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ReadProcessSummaries")
    public org.apromore.manager.model_portal.ReadProcessSummariesOutputMsgType readProcessSummaries(
        @WebParam(partName = "payload", name = "ReadProcessSummariesInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ReadProcessSummariesInputMsgType payload
    );

	@WebResult(name = "ReadEditSessionOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "ReadEditSession")
    public org.apromore.manager.model_portal.ReadEditSessionOutputMsgType readEditSession(
        @WebParam(partName = "payload", name = "ReadEditSessionInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.ReadEditSessionInputMsgType payload
    );

	@WebResult(name = "UpdateProcessOutputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal", partName = "payload")
    @WebMethod(operationName = "UpdateProcess")
    public org.apromore.manager.model_portal.UpdateProcessOutputMsgType updateProcess(
        @WebParam(partName = "payload", name = "UpdateProcessInputMsg", targetNamespace = "http://www.apromore.org/manager/model_portal")
        org.apromore.manager.model_portal.UpdateProcessInputMsgType payload
    );
}
