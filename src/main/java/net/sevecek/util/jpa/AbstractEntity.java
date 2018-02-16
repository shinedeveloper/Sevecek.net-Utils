package net.sevecek.util.jpa;

import java.io.*;

/**
 * <p>
 * Abstract superclass for all entity classes.
 * </p>
 * <p>
 * Contains universal implementation of {@link #equals(Object)} and {@link #hashCode()} methods
 * based only on the primary key ({@link #getId()} method).
 * This strategy is recommended in JPA.
 * </p>
 * <p>
 * The only thing left out to the subclasses is the {@link #testInstanceOf(Object)}
 * which MUST be implemented to prevent all entity classes being
 * equals-compatible (which would be wrong).
 * </p>
 *
 * <p>
 * Also note that the {@link net.sevecek.util.jpa.AbstractEntity} doesn't force programmers
 * to use the same primary key class in all entity classes.
 * The return type of the {@link #getId()} method can be overridden
 * by any object type,
 * such as {@link java.util.UUID}, {@link java.lang.Long} or {@link java.math.BigDecimal}.
 * </p>
 */
public abstract class AbstractEntity<PK> implements Serializable {

    public abstract PK getId();

    //-------------------------------------------------------


    /**
     * <p>
     * Standard equals method.<br/>
     * DO NOT override in subclasses.
     * If you want to change its behaviour, override
     * {@link #testInstanceOf(Object)} instead.
     * </p>
     *
     * <p>
     * The equals algorithm is based on comparing just the primary keys
     * as recommended in JPA (so that the equals is consistent with
     * EntityManager equality and database identity).
     * If both entities being compared have their primary key == <code>null</code>,
     * the equals returns <code>false</code>.
     * </p>
     *
     * <p>
     * This equals method complies with all 4 rules of equals contract
     * (reflexivity, symmetry, transitivity, stability)
     * but is also compatible with Hibernate lazy fetch proxies.
     * Of course, EclipseLink and OpenJPA are compatible too
     * as well as should any other custom database framework.
     * </p>
     *
     * @param other The other object this entity is compared with
     * @return <code>true</code> if the primary key of this entity and the other entity are equal,
     *         and the entity types are compatible according to {@link #testInstanceOf(Object)}
     *         and the primary keys are not <code>null</code>.
     *         Otherwise <code>false</code>.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (this.getId() == null) return false;
        if (!this.testInstanceOf(other)) return false;
        AbstractEntity<?> otherEntity = (AbstractEntity<?>) other;
        if (!otherEntity.testInstanceOf(this)) return false;

        return getId().equals(otherEntity.getId());
    }


    /**
     * <p>
     * This method should be override by concrete entity classes.
     * The rule of thumb is:
     * </p>
     * <p>
     * If the class you are writing SHOULD NOT be equals-compatible
     * with its superclass, the method MUST be OVERRIDDEN.
     * If the class you are writing SHOULD be equals-compatible,
     * you DO NOT OVERRIDE this method.
     * </p>
     * <p>
     * How to implement the method?
     * </p>
     * <pre>
     * protected boolean testInstanceOf(Object other) {
     *     return other instanceof YourClass;
     * }
     * </pre>
     *
     * @param other
     * @return
     */
    protected abstract boolean testInstanceOf(Object other);


    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}
