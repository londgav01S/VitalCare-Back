# Integración Wompi (Backend + Guía Front)

Esta guía documenta lo agregado al backend para usar Wompi como pasarela y qué necesita el Front en Render para completar el flujo.

## Resumen de la arquitectura
- Front solicita al backend una "sesión" de pago para una factura (invoice).
- Backend genera `reference`, `amountInCents`, `currency` y `signature` (hash de integridad).
- Front abre el Checkout de Wompi con esos datos usando la `publicKey`.
- Wompi notifica estados vía Webhook al backend. El backend valida la firma del evento y actualiza el estado del pago/factura.

## Endpoints backend
- POST `/api/payments/session`
  - Body:
    ```json
    { "invoiceId": "<UUID>", "currency": "COP" }
    ```
  - Respuesta:
    ```json
    {
      "reference": "INV-<uuid>",
      "amountInCents": 123400,
      "currency": "COP",
      "signature": "<md5>",
      "publicKey": "pub_test_..."
    }
    ```
- POST `/api/payments/webhook`
  - Headers: `X-Event-Signature: <hex>`
  - Body: objeto de evento Wompi (`transaction.updated`). El backend valida firma y persiste el estado.

## Variables de entorno (Render)
Configura en Render (o tu plataforma):
- `WOMPI_PUBLIC_KEY` (Llave pública)
- `WOMPI_PRIVATE_KEY` (Llave privada, para server-server si la usas)
- `WOMPI_EVENTS_SECRET` (Secreto de eventos para validar webhooks)
- `WOMPI_INTEGRITY_KEY` (Llave de integridad para generar `signature`)
- Opcional: `WOMPI_BASE_URL` (`https://sandbox.wompi.co/v1` para pruebas, producción por defecto)

Estas variables se leen desde `application.properties` con el prefijo `wompi.*`.

## Seguridad/CORS
- `POST /api/payments/session` y `POST /api/payments/webhook` se permiten sin JWT.
- El webhook se valida con `X-Event-Signature` calculado como:
  - `hex( SHA256( rawBody + WOMPI_EVENTS_SECRET ) )`

## Guía para el Front
1. Llamar a `POST /api/payments/session` con `invoiceId` y `currency`.
2. Con la respuesta, usar `publicKey`, `reference`, `amountInCents`, `currency`, `signature` para abrir el Checkout de Wompi.
3. Escuchar el resultado del Checkout. Para confiabilidad, consultar al backend o esperar que el backend confirme vía cambios de estado/consulta.

### Ejemplo React (Checkout Wompi)
```tsx
import { useEffect } from 'react';

export function openWompiCheckout(data) {
  // data: { publicKey, reference, amountInCents, currency, signature }
  const checkout = new (window as any).WidgetCheckout({
    currency: data.currency,
    amountInCents: data.amountInCents,
    reference: data.reference,
    publicKey: data.publicKey,
    signature: data.signature,
  });
  checkout.open((result) => {
    // result.transaction.status -> APPROVED, DECLINED, etc.
    // Recomendado: mostrar pantalla de "Procesando" y consultar al backend
  });
}
```

### Flujo recomendado en Front
- Paso 1: El usuario selecciona pagar la factura.
- Paso 2: Front llama a `/api/payments/session` y obtiene los datos.
- Paso 3: Front abre el widget/checkout con esos datos.
- Paso 4: Mostrar resultado y redirigir a una pantalla que consulte estado de la factura o pago.

## Estados y persistencia
- Modelos extendidos:
  - `Payment` incluye `reference`, `transactionId`, `status` y `method = WOMPI`.
  - Cuando `status = APPROVED`, la `Invoice` se marca `PAID` automáticamente.

## Pruebas locales
- Usa `WOMPI_BASE_URL=https://sandbox.wompi.co/v1` y llaves sandbox.
- Exponer webhook con `ngrok` o Render. Configura la URL del webhook en el dashboard de Wompi.

## Próximos pasos sugeridos
- Endpoint para consultar estado del pago por `reference` o `invoiceId`.
- Idempotencia en creación de pagos y reintentos en conciliación.
- Firmas HMAC con cabeceras adicionales si Wompi las añade en tu cuenta.
- Notificaciones en tiempo real al front (WebSocket) tras webhook.
