package prueba.com.example.demo.security;

public final class TenantContext {

    private static final ThreadLocal<Long> WORKSHOP_ID = new ThreadLocal<>();

    private TenantContext() {}

    public static void setWorkshopId(Long workshopId) {
        WORKSHOP_ID.set(workshopId);
    }

    public static Long getWorkshopId() {
        return WORKSHOP_ID.get();
    }

    public static Long requireWorkshopId() {
        Long id = WORKSHOP_ID.get();
        if (id == null) {
            throw new IllegalStateException("Tenant (workshopId) no presente en el contexto. ¿Token sin claim workshopId?");
        }
        return id;
    }

    public static void clear() {
        WORKSHOP_ID.remove();
    }
}
