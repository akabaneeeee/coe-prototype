package org.aclogistics.coe.domain.port;

/**
 * @author Rosendo Coquilla
 */
public interface IPdfRendererService {

    byte[] generate(String htmlContent);
}
