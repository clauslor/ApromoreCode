package org.apromore.canoniser.service;

import org.apromore.model.CanoniseVersionInputMsgType;
import org.apromore.model.CanoniseVersionOutputMsgType;
import org.apromore.model.GenerateAnnotationInputMsgType;
import org.apromore.model.GenerateAnnotationOutputMsgType;

/**
 *
 *
 */
@Deprecated //TODO this should not be used anymore!!
public interface CanoniserManager {

    public GenerateAnnotationOutputMsgType generateAnnotation(GenerateAnnotationInputMsgType payload);

//    public CanoniseProcessOutputMsgType canoniseProcess(CanoniseProcessInputMsgType payload);

    @Deprecated
    public CanoniseVersionOutputMsgType canoniseVersion(CanoniseVersionInputMsgType payload);
}