# Reporte de Identificación de Vulnerabilidades

## 1. Introducción
Este documento detalla el análisis estático y dinámico realizado a la aplicación **Sistema SeguridadApp** utilizando la herramienta MobSF (Mobile Security Framework), conforme a los requerimientos de la pauta de seguridad.

## 2. Herramientas Utilizadas
- **MobSF (Mobile Security Framework):** Utilizado para el análisis estático del paquete APK en su versión de producción.
- **Analizador estático de manifiesto y código fuente (SAST):** Para revisar configuraciones de compilación, permisos y rastreo de dependencias.

## 3. Vulnerabilidades y Hallazgos Identificados
A partir del análisis generado en el reporte técnico, se identificaron los siguientes puntos clave de seguridad:

* **Certificado de Depuración (Mitigado):** En las primeras fases de desarrollo en modo debug, la aplicación presentaba alertas por uso de certificados de prueba. Esto se solucionó compilando y firmando el APK utilizando un almacén de claves de producción.
* **Modo Depuración Habilitado (Mitigado):** Se configuró la directiva `isDebuggable = false` en el archivo `build.gradle.kts` para evitar que herramientas externas puedan depurar la aplicación en ejecución.
* **Permisos Sensibles de Hardware y Ubicación:** La aplicación declara permisos de cámara y geolocalización. Estos permisos son requeridos por las funcionalidades de mapas y captura de imágenes, los cuales se solicitan de forma dinámica en tiempo de ejecución para evitar riesgos de privacidad.
* **Control de Red:** Se verificó mediante la configuración de seguridad que el tráfico en texto plano está completamente deshabilitado, previniendo la interceptación de datos sensibles.