package com.jhobor.ddc.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.jhobor.ddc.entity.ShopCar;

import com.jhobor.ddc.greendao.ShopCarDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig shopCarDaoConfig;

    private final ShopCarDao shopCarDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        shopCarDaoConfig = daoConfigMap.get(ShopCarDao.class).clone();
        shopCarDaoConfig.initIdentityScope(type);

        shopCarDao = new ShopCarDao(shopCarDaoConfig, this);

        registerDao(ShopCar.class, shopCarDao);
    }
    
    public void clear() {
        shopCarDaoConfig.clearIdentityScope();
    }

    public ShopCarDao getShopCarDao() {
        return shopCarDao;
    }

}
