package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.application.service.dto.command.CreateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateAplicacionCommand;
import com.jcaa.usersmanagement.domain.enums.CategoriaAplicacion;
import com.jcaa.usersmanagement.domain.enums.Licencia;
import com.jcaa.usersmanagement.domain.enums.SistemaOperativo;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionCorreo;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionWeb;
import com.jcaa.usersmanagement.domain.valueobject.Precio;
import com.jcaa.usersmanagement.domain.valueobject.TamanoInstalador;
import java.math.BigDecimal;

/** Datos de prueba compartidos por los tests de Aplicacion. */
public final class AplicacionTestData {

  public static final String ID = "8f684dc4-0c67-4813-8d58-7b5e7f7937b8";

  private AplicacionTestData() {}

  public static AplicacionModel model() {
    return model(Licencia.OPEN_SOURCE, "0");
  }

  public static AplicacionModel model(final Licencia licencia, final String precio) {
    return AplicacionModel.create(
        new AplicacionId(ID),
        new AplicacionNombre("Visual Studio Code"),
        "Microsoft",
        CategoriaAplicacion.DESARROLLO,
        "TypeScript",
        "JavaScript",
        false,
        false,
        64,
        SistemaOperativo.MULTIPLATAFORMA,
        "4 GB de RAM",
        licencia,
        new Precio(new BigDecimal(precio)),
        "Editor de codigo",
        new AplicacionWeb("https://code.visualstudio.com"),
        new AplicacionCorreo("soporte@ejemplo.com"),
        new TamanoInstalador(new BigDecimal("95.50")));
  }

  public static CreateAplicacionCommand createCommand() {
    return new CreateAplicacionCommand(
        "Visual Studio Code",
        "Microsoft",
        "DESARROLLO",
        "TypeScript",
        "JavaScript",
        false,
        false,
        64,
        "MULTIPLATAFORMA",
        "4 GB de RAM",
        "OPEN_SOURCE",
        new BigDecimal("0"),
        "Editor de codigo",
        "https://code.visualstudio.com",
        "soporte@ejemplo.com",
        new BigDecimal("95.50"));
  }

  public static UpdateAplicacionCommand updateCommand() {
    return new UpdateAplicacionCommand(
        ID,
        "Visual Studio Code",
        "Microsoft",
        "DESARROLLO",
        "TypeScript",
        "JavaScript",
        false,
        false,
        64,
        "MULTIPLATAFORMA",
        "8 GB de RAM",
        "OPEN_SOURCE",
        new BigDecimal("0"),
        "Editor de codigo",
        "https://code.visualstudio.com",
        "soporte@ejemplo.com",
        new BigDecimal("95.50"));
  }
}
