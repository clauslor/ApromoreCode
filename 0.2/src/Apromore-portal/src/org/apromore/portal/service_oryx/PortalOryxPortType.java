package org.apromore.portal.service_oryx;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Tue Feb 01 09:34:33 CET 2011
 * Generated source version: 2.2.9
 * 
 */
 
@WebService(targetNamespace = "http://www.apromore.org/portal/service_oryx", name = "PortalOryxPortType")
@XmlSeeAlso({org.apromore.portal.model_oryx.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PortalOryxPortType {

    @WebResult(name = "WriteProcessOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "WriteProcess")
    public org.apromore.portal.model_oryx.WriteProcessOutputMsgType writeProcess(
        @WebParam(partName = "payload", name = "WriteProcessInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.WriteProcessInputMsgType payload
    );

    @WebResult(name = "WriteNewAnnotationOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "WriteNewAnnotation")
    public org.apromore.portal.model_oryx.WriteNewAnnotationOutputMsgType writeNewAnnotation(
        @WebParam(partName = "payload", name = "WriteNewAnnotationInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.WriteNewAnnotationInputMsgType payload
    );

	@WebResult(name = "WriteNewProcessOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "WriteNewProcess")
    public org.apromore.portal.model_oryx.WriteNewProcessOutputMsgType writeNewProcess(
        @WebParam(partName = "payload", name = "WriteNewProcessInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.WriteNewProcessInputMsgType payload
    );

    @WebResult(name = "ReadNativeOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "ReadNative")
    public org.apromore.portal.model_oryx.ReadNativeOutputMsgType readNative(
        @WebParam(partName = "payload", name = "ReadNativeInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.ReadNativeInputMsgType payload
    );

	@WebResult(name = "CloseSessionOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "CloseSession")
    public org.apromore.portal.model_oryx.CloseSessionOutputMsgType closeSession(
        @WebParam(partName = "payload", name = "CloseSessionInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.CloseSessionInputMsgType payload
    );

	@WebResult(name = "WriteAnnotationOutputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx", partName = "payload")
    @WebMethod(operationName = "WriteAnnotation")
    public org.apromore.portal.model_oryx.WriteAnnotationOutputMsgType writeAnnotation(
        @WebParam(partName = "payload", name = "WriteAnnotationInputMsg", targetNamespace = "http://www.apromore.org/portal/model_oryx")
        org.apromore.portal.model_oryx.WriteAnnotationInputMsgType payload
    );
}