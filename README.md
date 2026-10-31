# Sistema SeguridadApp #

## Descripción
Este proyecto es una aplicación Android desarrollada en Java que implementa medidas robustas de seguridad para proteger la información local, la gestión de sesiones y el uso seguro de componentes nativos, previniendo vulnerabilidades comunes del OWASP Mobile Top 10.

## Vulnerabilidades Identificadas
- Inyección SQL (Mitigada mediante consultas parametrizadas).
- Comunicación no segura en texto plano (Mitigada con Network Security Configuration).
- Exposición de datos sensibles en almacenamiento local (Mitigada con almacenamiento cifrado).

## Mejoras Implementadas
- Cifrado de datos locales.
- Forzado de comunicación segura y bloqueo de tráfico sin cifrar.
- Validación estricta de sesiones de usuario y ofuscación de código.

## Documentación
- [Vulnerabilidades](vulnerabilities.md)
- [Best Practices](best_practices.md)
- [Security Tips](security_tips.md)
- [Security Improvement Program](security_improvement_program.md)

## Reportes de Seguridad
- [Reporte de análisis estático (PDF)](vulnerability_report.pdf)
- [Reporte de análisis dinámico (PDF)](vulnerability_dynamicReport.pdf)

## Informe Técnico
- [Informe de análisis y mejora de seguridad de una aplicación Android](Informe_Seguridad_Aplicacion_Android(Alumno).pdf)

## Cómo Ejecutar la Aplicación de Forma Segura
1. Clonar el repositorio en su equipo local.
2. Importar el proyecto utilizando Android Studio.
3. Crear un archivo llamado `local.properties` en la raíz del proyecto si no existe.
4. Agregar la clave de API de Google Maps en dicho archivo con el siguiente formato:
   `AIzaSyCq4JM--e0DBKAws3xNkNzGIcKaT2ZiNsc`
5. Sincronizar el proyecto con Gradle (`Sync Now`).
6. Ejecutar la aplicación en un dispositivo físico o emulador Android.
7. Asegurarse de que los permisos de red, ubicación y cámara requeridos estén debidamente concedidos.

## Reporte de Vulnerabilidades
El reporte detallado de las pruebas de seguridad y el análisis estático se encuentra en el archivo `vulnerability_report.pdf`, complementado por el informe de simulación de seguridad y análisis dinámico en tiempo de ejecución registrado en el archivo `vulnerability_dynamicReport.pdf`.