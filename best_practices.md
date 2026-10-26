# Prácticas Recomendadas (Best Practices)

Este documento describe las buenas prácticas de desarrollo seguro implementadas en la arquitectura de **Sistema SeguridadApp**.

## 1. Almacenamiento Cifrado de Datos Sensibles
- **Implementación:** Se utilizó la librería oficial de criptografía mediante la clase `EncryptedSharedPreferences`.
- **Mejora de Seguridad:** Los tokens de sesión y credenciales del usuario no se almacenan en texto plano en las preferencias compartidas del dispositivo, sino que se cifran utilizando claves respaldadas por el contenedor de hardware del sistema, previniendo la extracción de datos ante accesos no autorizados al almacenamiento local.

## 2. Configuración Segura de Comunicaciones de Red
- **Implementación:** Se configuró un archivo dedicado de seguridad de red asignado en el manifiesto de la aplicación.
- **Mejora de Seguridad:** Se estableció explícitamente que no se permite tráfico en texto plano, lo que fuerza a que todas las comunicaciones y peticiones se realicen exclusivamente bajo protocolos cifrados, mitigando ataques de intermediario.

## 3. Minimización de Permisos y Ciclo de Vida
- **Implementación:** Los permisos de cámara y geolocalización no se autoevalúan de forma agresiva al iniciar la app, sino que emplean lanzadores de contratos de actividad para solicitarlos de manera contextual y dinámica cuando el usuario interactúa explícitamente con las funciones del mapa o de la cámara.