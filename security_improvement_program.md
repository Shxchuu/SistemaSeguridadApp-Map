# Programa de Mejora de Seguridad (App Security Improvement Program)

## 1. Objetivo del Programa
Establecer un ciclo de evaluación, control y optimización continua de los estándares de seguridad en **Sistema SeguridadApp**, asegurando la resiliencia frente a nuevas vulnerabilidades del ecosistema Android.

## 2. Proceso de Revisión Periódica
- **Fase de Desarrollo:** Revisiones estáticas locales del código mediante análisis de dependencias antes de cada integración de código.
- **Fase de Compilación:** Ejecución de análisis estáticos automatizados para verificar la calificación de riesgo del paquete APK previo a su entrega final.

## 3. Métricas Clave de Seguridad
- **Calificación de Riesgo:** Medición del puntaje obtenido en las herramientas de análisis, alcanzando un nivel de riesgo bajo con calificación óptima.
- **Ausencia de Alertas Críticas:** Validación de que las directrices de depuración y los canales de red cifrados se mantengan sin alertas de nivel alto.

## 4. Plan de Acción para Futuras Mejoras
1. Implementar fijación de certificados si la aplicación pasa a consumir servicios web externos en futuras versiones.
2. Añadir validación de integridad del binario contra modificaciones o empaquetados maliciosos de terceros.