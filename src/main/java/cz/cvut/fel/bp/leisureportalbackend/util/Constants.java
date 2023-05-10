package cz.cvut.fel.bp.leisureportalbackend.util;

import cz.cvut.fel.bp.leisureportalbackend.model.UserType;

public final class Constants {

    /**
     * Default user role.
     */
    public static final UserType DEFAULT_ROLE = UserType.GUEST;

    private Constants() {
        throw new AssertionError();
    }
}
