package co.edu.uniquindio.vitalcareback.Model.billing;

/**
 * Estados posibles de una transacción reportados por Wompi.
 * Se alinean con la documentación oficial.
 */
public enum PaymentStatus {
    PENDING, // Creada pero aún no aprobada
    APPROVED, // Pagada correctamente
    DECLINED, // Rechazada por el medio de pago
    ERROR, // Error técnico
    VOIDED // Anulada
}
