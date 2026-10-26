# Consejos de Seguridad (Security Tips)

Se detallan las medidas de protección específicas integradas para contrarrestar vectores de ataque comunes en aplicaciones móviles.

## 1. Protección contra Inyección SQL (SQL Injection)
- **Implementación:** En los componentes de persistencia local y bases de datos, se estructuraron las consultas utilizando parámetros ligados en lugar de concatenación directa de cadenas de texto.
- **Mejora de Seguridad:** Se neutraliza el riesgo de que entradas manipuladas por usuarios malintencionados alteren la lógica de las consultas ejecutadas en la base de datos local.

## 2. Autenticación y Autorización Seguras
- **Implementación:** La actividad principal valida obligatoriamente la integridad de la sesión activa a través de la capa de almacenamiento cifrado antes de permitir el acceso a las funciones del mapa y geolocalización.
- **Mejora de Seguridad:** Si no existe una sesión válida o el usuario ha cerrado sesión, la aplicación destruye el contexto actual y redirige inmediatamente al usuario al módulo de autenticación, evitando el acceso mediante saltos de pantallas.

## 3. Ofuscación de Código con R8 / ProGuard
- **Implementación:** Se habilitó la minificación y optimización del código en las compilaciones de producción junto con reglas personalizadas de ofuscación.
- **Mejora de Seguridad:** Se dificulta la ingeniería inversa y el análisis estático del código al renombrar clases y eliminar trazas de registro internas.