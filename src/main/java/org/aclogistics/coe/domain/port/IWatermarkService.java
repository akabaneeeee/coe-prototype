package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.enumeration.BusinessUnit;

/**
 * @author Rosendo Coquilla
 */
public interface IWatermarkService {

    byte[] addWatermark(byte[] pdfBytes, BusinessUnit businessUnit);
}
