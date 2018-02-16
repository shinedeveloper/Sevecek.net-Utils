package net.sevecek.tablemodel;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.table.*;

public class BeanTableModel<T> extends AbstractTableModel implements TableModel {

    private static final Pattern COLUMN_NAME_REGEX = Pattern.compile("(\\p{Lu}[^\\p{Lu}])");

    private Class<?> beanClass;
    private boolean userBeanClass;

    private PropertyDescriptor[] propertyDescriptors;
    private PropertyDescriptor[] selectedPropertyDescriptors;
    private String[] selectedPropertyNames;
    private boolean userSelectedPropertyNames;
    private boolean excludeClassProperty = true;

    private String[] columnNames;
    private boolean userColumnNames;

    private List rows = new LinkedList();

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    public Class<?> getBeanClass() {
        return beanClass;
    }


    public void setBeanClass(Class<T> clazz) {
        Class<?> oldValue = beanClass;
        userBeanClass = (clazz != null);
        if (clazz == null) {
            clazz = detectBeanClassFromList();
        }
        if ((oldValue == null && clazz == null)
                || (oldValue != null && oldValue.equals(clazz))) {
            return;  // No change
        }

        beanClass = clazz;

        refreshClassMetadata();
        propertyChangeSupport.firePropertyChange("beanClass", oldValue, beanClass);
    }


    private void refreshClassMetadata() {
        refreshPropertyDescriptors();
        refreshSelectedProperties();
        refreshColumnNames();

        fireTableStructureChanged();
    }


    private void refreshPropertyDescriptors() {
        if (beanClass == null) {
            propertyDescriptors = null;
            return;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] tmpPropertyDescriptors = beanInfo.getPropertyDescriptors();
            if (excludeClassProperty) {
                PropertyDescriptor[] correctPropertyDescriptors = new PropertyDescriptor[tmpPropertyDescriptors.length - 1];
                for (int i = 0, j = 0; i < tmpPropertyDescriptors.length; i++) {
                    if (!tmpPropertyDescriptors[i].getName().equals("class")) {
                        correctPropertyDescriptors[j] = tmpPropertyDescriptors[i];
                        j++;
                    }
                    propertyDescriptors = correctPropertyDescriptors;
                }
            } else {
                propertyDescriptors = tmpPropertyDescriptors;
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);   // TODO
        }
    }


    private void refreshSelectedProperties() {
        if (!userSelectedPropertyNames) {
            String[] oldValue = selectedPropertyNames;
            if (propertyDescriptors == null) {
                selectedPropertyDescriptors = null;
                selectedPropertyNames = null;
            } else {
                selectedPropertyDescriptors = new PropertyDescriptor[propertyDescriptors.length];
                selectedPropertyNames = new String[propertyDescriptors.length];
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    selectedPropertyDescriptors[i] = propertyDescriptors[i];
                    selectedPropertyNames[i] = propertyDescriptors[i].getName();
                }
            }
            propertyChangeSupport.firePropertyChange("propertyNames", oldValue, selectedPropertyNames);
        } else {
            selectedPropertyDescriptors = new PropertyDescriptor[selectedPropertyNames.length];
            for (int i = 0; i < selectedPropertyNames.length; i++) {
                String propertyName = selectedPropertyNames[i];
                PropertyDescriptor propertyDescriptor = findPropertyDescriptor(propertyName);
                selectedPropertyDescriptors[i] = propertyDescriptor;
            }
        }
    }


    private PropertyDescriptor findPropertyDescriptor(String propertyName) {
        if (propertyDescriptors != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(propertyName)) {
                    return propertyDescriptor;
                }
            }
        }
        try {
            return new PropertyDescriptor(propertyName, null, null);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);   // TODO
        }
    }


    private void refreshColumnNames() {
        if (!userColumnNames) {
            if (selectedPropertyNames == null) {
                columnNames = null;
            } else {
                columnNames = new String[selectedPropertyNames.length];
                for (int i = 0; i < selectedPropertyNames.length; i++) {
                    columnNames[i] = constructColumnNameFromPropertyName(selectedPropertyNames[i]);
                }
            }
        }
    }


    private String constructColumnNameFromPropertyName(String propertyName) {
        Matcher matcher = COLUMN_NAME_REGEX.matcher(propertyName);
        propertyName = matcher.replaceAll(" $1");
        if (propertyName.charAt(0) == ' ') {
            propertyName = propertyName.substring(1);
        }

        String upperCase = propertyName.toUpperCase(Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(upperCase.codePointAt(0));

        int pos;
        if (Character.isHighSurrogate(propertyName.charAt(0)) && Character.isLowSurrogate(propertyName.charAt(1))) {
            pos = 2;
        } else {
            pos = 1;
        }
        builder.append(propertyName, pos, propertyName.length());
        return builder.toString();
    }

    //-------------------------------------------------------------------------


    public void setPropertyNames(String[] newPropertyNames) {
        if (newPropertyNames == null) {
            if (!userSelectedPropertyNames) return;   // No change

            String[] oldValue = selectedPropertyNames;
            userSelectedPropertyNames = false;
            refreshSelectedProperties();
            refreshColumnNames();

            fireTableStructureChanged();
            propertyChangeSupport.firePropertyChange("propertyNames", oldValue, newPropertyNames);
        } else {
            String[] oldValue = selectedPropertyNames;
            selectedPropertyNames = newPropertyNames;
            userSelectedPropertyNames = true;
            refreshSelectedProperties();
            refreshColumnNames();

            fireTableStructureChanged();
            propertyChangeSupport.firePropertyChange("propertyNames", oldValue, newPropertyNames);
        }
    }

    //-------------------------------------------------------------------------


    public String[] getPropertyNames() {
        return selectedPropertyNames;
    }

    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void setRows(List<T> beans) {
        if (beans == null) {
            beans = new LinkedList();
        }

        rows = beans;
        if (!userBeanClass && !rows.isEmpty()) {
            Class<?> oldBeanClass = beanClass;
            setBeanClass(null);
            // The method above triggers fireTableStructureChanged()
            if (oldBeanClass != beanClass) return;
        }
        fireTableDataChanged();
    }


    @SuppressWarnings("unchecked")
    private Class<T> detectBeanClassFromList() {
        if (rows.size() == 0) {
            return null;
        }
        Iterator<?> iterator = rows.iterator();
        Object firstBean = iterator.next();
        Class<?> foundClass = firstBean.getClass();
        while (iterator.hasNext()) {
            Object otherBean = iterator.next();
            if (!otherBean.getClass().isAssignableFrom(foundClass)) {
                foundClass = leastCommonSuperClass(otherBean.getClass(), foundClass);
            }
        }
        return (Class<T>) foundClass;
    }


    private Class<?> leastCommonSuperClass(Class<?> firstClass, Class<?> secondClass) {
        Class[] firstClassInheritanceChain = resolveInheritanceChain(firstClass);
        Class[] secondClassInheritanceChain = resolveInheritanceChain(secondClass);
        Class lastMatching = Object.class;
        for (int i = 0; i < Math.min(firstClassInheritanceChain.length, secondClassInheritanceChain.length); i++) {
            if (!firstClassInheritanceChain[i].equals(secondClassInheritanceChain[i])) {
                break;
            }
            lastMatching = firstClassInheritanceChain[i];
        }
        return lastMatching;
    }


    private Class[] resolveInheritanceChain(Class<?> aClass) {
        LinkedList<Class> inheritanceChain = new LinkedList<Class>();
        while (!aClass.equals(Object.class)) {
            inheritanceChain.addFirst(aClass);
            aClass = aClass.getSuperclass();
        }
        inheritanceChain.addFirst(Object.class);
        return inheritanceChain.toArray(new Class[inheritanceChain.size()]);
    }

    //-------------------------------------------------------------------------


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


    public String[] getColumnNames() {
        if (columnNames == null) {
            return new String[] {"1", "2", "3"};
        }
        return columnNames;
    }


    public void setColumnNames(String[] names) {
        String[] oldValue = columnNames;
        if (names == null) {
            userColumnNames = false;
            refreshColumnNames();
        } else {
            userColumnNames = true;
            columnNames = names;
        }

        fireTableStructureChanged();
        propertyChangeSupport.firePropertyChange("columnNames", oldValue, columnNames);
    }


    @Override
    public int getColumnCount() {
        return getColumnNames().length;
    }


    @Override
    public int getRowCount() {
        return rows.size();
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        validateColumnIndex(columnIndex);
        if (selectedPropertyDescriptors == null) {
            return Object.class;
        } else {
            return selectedPropertyDescriptors[columnIndex].getPropertyType();
        }
    }


    private void validateColumnIndex(int columnIndex) {
        if (columnIndex < 0) {
            throw new IndexOutOfBoundsException("Negative column numbers are not allowed");
        }
        int columnCount = getColumnCount();
        if (columnIndex >= columnCount) {
            throw new IndexOutOfBoundsException("Column number " + columnIndex + " was requested but table has only " + columnCount + " columns");
        }
    }


    private void validateRowIndex(int rowIndex) {
        if (rowIndex < 0) {
            throw new IndexOutOfBoundsException("Negative row numbers are not allowed");
        }
        if (rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException("Row number " + rowIndex + " was requested but table has only " + rows.size() + " rows");
        }
    }


    @SuppressWarnings("unchecked")
    public T getBeanAt(int rowIndex) {
        validateRowIndex(rowIndex);
        return (T) rows.get(rowIndex);
    }


    public int findBean(T bean) {
        if (bean == null) throw new NullPointerException("findBean(null)");
        Iterator<?> iterator = rows.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (bean.equals(iterator.next())) {
                return i;
            }
        }
        return -1;
    }


    @SuppressWarnings("unchecked")
    public void addBean(T bean) {
        if (bean == null) throw new NullPointerException("addBean(null)");
        rows.add(bean);
        if (!userBeanClass) {
            Class<?> oldBeanClass = beanClass;
            setBeanClass(null);
            // The method above triggers fireTableStructureChanged()
            if (oldBeanClass != beanClass) return;
        }
        int row = rows.size() - 1;
        fireTableRowsInserted(row, row);
    }


    public T removeBean(T bean) {
        if (bean == null) throw new NullPointerException("removeBean(null)");
        int rowIndex = findBean(bean);
        if (rowIndex > -1) {
            return removeBean(rowIndex);
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public T removeBean(int rowIndex) {
        Object originalBean = rows.remove(rowIndex);
        if (!userBeanClass) {
            Class<?> oldBeanClass = beanClass;
            setBeanClass(null);
            // The method above triggers fireTableStructureChanged()
            if (oldBeanClass != beanClass) return (T) originalBean;
        }
        fireTableRowsDeleted(rowIndex, rowIndex);
        return (T) originalBean;
    }


    public void updateBean(T bean) {
        if (bean == null) throw new NullPointerException("updateBean(null)");
        int rowIndex = findBean(bean);
        if (rowIndex > -1) {
            updateBean(rowIndex, bean);
        }
    }


    @SuppressWarnings("unchecked")
    public void updateBean(int rowIndex, T bean) {
        if (bean == null) throw new NullPointerException("updateBean(" + rowIndex + ", null)");
        Object originalBean = rows.get(rowIndex);
        if (bean != originalBean) {
            rows.set(rowIndex, bean);
            if (!userBeanClass) {
                Class<?> oldBeanClass = beanClass;
                setBeanClass(null);
                // The method above triggers fireTableStructureChanged()
                if (oldBeanClass != beanClass) return;
            }
        }
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    //----------------------------------------------------------------


    @Override
    public String getColumnName(int columnIndex) {
        validateColumnIndex(columnIndex);
        return getColumnNames()[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object bean = getBeanAt(rowIndex);
        validateColumnIndex(columnIndex);
        if (selectedPropertyDescriptors == null) {
            return "---";
        }
        PropertyDescriptor property = selectedPropertyDescriptors[columnIndex];
        Method readMethod = property.getReadMethod();
        if (readMethod != null) {
            try {
                return readMethod.invoke(bean);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);  // TODO
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);  // TODO
            }
        } else {
            return "---";
        }
    }

    //-------------------------------------------------------------------------

    public void setExcludeClassProperty(boolean value) {
        boolean oldValue = excludeClassProperty;
        if (oldValue == value) return;      // No change
        excludeClassProperty = value;

        refreshClassMetadata();
        propertyChangeSupport.firePropertyChange("excludeClassProperty", oldValue, selectedPropertyNames);
    }


    public boolean isExcludeClassProperty() {
        return excludeClassProperty;
    }
}
