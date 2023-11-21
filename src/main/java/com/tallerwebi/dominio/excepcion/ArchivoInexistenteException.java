package com.tallerwebi.dominio.excepcion;

public class ArchivoInexistenteException extends Exception {
  public ArchivoInexistenteException(String errorMessage) {
    super(errorMessage);
  }
}
