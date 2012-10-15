/**
 *
 */
package net.escritoriodigital.components.model;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author André Fabbro
 * @category Model
 * @created 04/05/2010
 * @description Objeto Model Base
 */
@MappedSuperclass
public abstract class ModelBase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8467998646493276020L;
	@Id @Column(name="ID")
	@GeneratedValue(generator="hibseq") @GenericGenerator(name="hibseq", strategy = "increment")
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelBase other = (ModelBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Object invokeGetOrSetMethod(String attribute, String prefix, Object arg) throws Exception {

        if(attribute != null) {
                String methodName = prefix + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);

                Class<?> tClass = getClass();
                Object[] args = null;
                Class<?>[] argTypes = null;

                try {

                        if(prefix.equals("set")) {

                                if(arg == null) return null;

                                args = new Object[1];

                                // find the type of attribute
                                Field fld = null;
                                try {
                                        fld = tClass.getDeclaredField(attribute);
                                } catch (NoSuchFieldException e) {
                                        // means the field could be on the super class
                                        Class<?> superclass = tClass.getSuperclass();
                                        fld = superclass.getDeclaredField(attribute);
                                }

                                argTypes = new Class[1];
                                argTypes[0] = fld.getType();

                                if(fld.getType().equals(Class.forName("java.lang.Boolean"))) {

                                        if(arg.getClass().getSimpleName().equals("Boolean")) {
                                                args[0] = arg;
                                        } else {
                                            if(arg.toString().equals("1") || arg.equals("true")) args[0] = new Boolean(true);
                                            else if(arg.toString().equals("0") || arg.equals("false")) args[0] = new Boolean(false);
                                            else args[0] = new Boolean((String) arg.toString());
                                        }

                                } else {

                                        //sometimes the arg could still have in boolean type, so we convert here
                                        Object arg2;
                                        if(arg instanceof Boolean) {
                                                //Boolean temp = (Boolean) arg;
                                                arg2 = new String("");
                                        } else {
                                                arg2 = arg;
                                        }

                                        // create the object with correct type
                                        Class<?> partypes[] = new Class[1];
                                        partypes[0] = String.class;

                                        Constructor<?> ct = fld.getType().getConstructor(partypes);
                                        args[0] = ct.newInstance(arg2);
                                }
                        }


                        Method invokeMethod = tClass.getMethod(methodName, argTypes);
                        if(prefix.equals("get")) {
                                return invokeMethod.invoke(this);
                        } else {
                                invokeMethod.invoke(this, args);
                                return null;
                        }

                } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new Exception("IllegalArgumentException calling method " + methodName + " on " + tClass.getName());
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new Exception("IllegalAccessException calling method " + methodName + " on " + tClass.getName());
                } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new Exception("InvocationTargetException calling method " + methodName + " on " + tClass.getName());
                } catch (SecurityException e) {
                        e.printStackTrace();
                        throw new Exception("SecurityException calling method " + methodName + " on " + tClass.getName());
                } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new Exception("NoSuchMethodException calling method " + methodName + " on " + tClass.getName());
                }
        }

        return null;
}

}
