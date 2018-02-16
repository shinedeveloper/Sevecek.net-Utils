package net.sevecek.util.jpa;

import java.io.*;
import java.util.*;

public interface Dao<PK extends Serializable,
                     E extends AbstractEntity<PK>> {

    List<E> findAll(int firstItem, int count);

    E find(PK id);

    E add(E entity);

    E update(E entity);

    E delete(E entity);

}
