package com.db.utils;

import com.db.entity.PosTag;
import com.db.factory.DictFactory;
import javafx.geometry.Pos;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class PosUtilsDB {
    private static Session session;

    static {
        session = DictFactory.getSession();
    }

    public static PosTag byName(String name) {
        PosTag code = null;
        List<Object> list = session.createQuery("from " + PosTag.class.getSimpleName() + " where name = :name").setParameter("name", name).list();
        if (list != null && list.size() > 0) {
            code = (PosTag) list.get(0);
        }
        return code;
    }

    public static PosTag byId(int id) {
        return (PosTag) session.get(PosTag.class, id);
    }

    public static List<PosTag> all() {
        return session.createQuery("from " + PosTag.class.getSimpleName()).list();
    }

}
