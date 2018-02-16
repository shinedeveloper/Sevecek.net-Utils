package net.sevecek.util.spring;

import org.springframework.dao.*;
import org.springframework.dao.support.*;
import org.springframework.orm.jpa.*;

public class DefaultJpaExceptionTranslator implements PersistenceExceptionTranslator {

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EntityManagerFactoryUtils.convertJpaAccessExceptionIfPossible(ex);
    }
}
