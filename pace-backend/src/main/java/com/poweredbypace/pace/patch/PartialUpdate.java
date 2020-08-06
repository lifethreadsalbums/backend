package com.poweredbypace.pace.patch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.exception.PartialUpdateException;
import com.poweredbypace.pace.util.SpringContextUtil;

/**
 * Helper class for performing partial updates on Entities
 * having the delta object provided.
 */
public class PartialUpdate<Entity extends BaseEntity> {
	
	Repositories repos;
	
	/**
	 * Updates given instance of Entity with patches, i.e. for a given
	 * list of changes to apply, the entity is being updated.
	 *  
	 * @param entity object to update
	 * @param patches list of changes to apply
	 * @return instance passed as first argument. not constructing any new instances.
	 */
	public Entity getUpdated(final Entity entity, final List<Patch> patches) {
		
		try {
		Preconditions.checkNotNull(entity);
		Preconditions.checkNotNull(patches);
		
		final ObjectMapper oMapper = new ObjectMapper();
		
		for(Patch patch: patches) {
			final String[] pathItems = patch.getPath().split("\\.");
			Object currInstance = entity;
			
			if(pathItems.length > 1) {
				for(int i = 0; i < pathItems.length - 1; i++) {
					currInstance = invokeGetter(currInstance, pathItems[i]);
				}
			}
			
			invokeSetter(
				patch, currInstance,
				findMethod(currInstance, pathItems[pathItems.length - 1], "set"),
				oMapper);
		}
		
		return entity;
		} catch (Exception ex) {
			throw new PartialUpdateException(ex);
		}
	}
	
	@PostConstruct
	private void init() {
		repos = new Repositories(SpringContextUtil.getBeanFactory());
	}
	
	@SuppressWarnings("unchecked")
	private void invokeSetter(Patch patch, Object entity, Method setter, ObjectMapper oMapper)
			throws NumberFormatException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException, JsonParseException,
			JsonMappingException, IOException {
		switch(patch.getType()) {
		case REPLACE: {
			invokeValueSetter(entity, setter, patch.getVal(), oMapper);
			break;
		}
		case SET_ID: {
			if(patch.getVal() != null) {
				// retrieve item from repository and pass into setter
				setter.invoke(
					entity,
					getRepository((Class<? extends BaseEntity>)setter.getParameterTypes()[0])
						.findOne(Long.parseLong(patch.getVal())));
			} else {
				// set value to null
				setter.invoke(entity, (Object)null);
			}
			break;
		}
		case ADD_SET: {
			Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>)setter.getParameterTypes()[0];
			JpaRepository<BaseEntity, Long> repo = getRepository(clazz);
			// Map the JSON (patch.getVal()) to the clazz, then save new instance, finally pass to the setter.
			setter.invoke(entity, repo.save((BaseEntity)oMapper.readValue(patch.getVal(), clazz)));
			break;
		}
		default: break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private JpaRepository<BaseEntity, Long> getRepository(Class<? extends BaseEntity> type) {
		return (JpaRepository<BaseEntity, Long>) repos.getRepositoryFor(type);
	}
	
	private static void invokeValueSetter(
			final Object instance, final Method method, final String val, final ObjectMapper oMapper)
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
					JsonParseException, JsonMappingException, IOException {
		final Class<?> type = method.getParameterTypes()[0];

		if(type.equals(int.class) || type.equals(Integer.class)) {
			method.invoke(instance, Integer.parseInt(val));
		} else if(type.equals(byte.class) || type.equals(Byte.class)) {
			method.invoke(instance, Byte.parseByte(val));
		} else if(type.equals(short.class) || type.equals(Short.class)) {
			method.invoke(instance, Short.parseShort(val));
		} else if(type.equals(long.class) || type.equals(Long.class)) {
			method.invoke(instance, Long.parseLong(val));
		} else if(type.equals(float.class) || type.equals(Float.class)) {
			method.invoke(instance, Float.parseFloat(val));
		} else if(type.equals(double.class) || type.equals(Double.class)) {
			method.invoke(instance, Double.parseDouble(val));
		} else if(type.equals(boolean.class) || type.equals(Boolean.class)) {
			method.invoke(instance, Boolean.parseBoolean(val));
		} else if(type.equals(char.class) || type.equals(Character.class) ||
				type.equals(String.class)) {
			method.invoke(instance, val);
		} else {
			method.invoke(
				instance,
				oMapper.readValue(val, type));
		}
	}
	
	private static Object invokeGetter(final Object instance, final String field)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return findMethod(instance, field, "get").invoke(instance);
	}
	
	private static Method findMethod(final Object instance, final String field, final String prefix) {
		final String methodLowerCase = prefix.toLowerCase() + field.toLowerCase();
		for(Method method: instance.getClass().getMethods()) {
			if(method.getName().toLowerCase().equals(methodLowerCase)) {
				return method;
			}
		}
		return null;
	}
	
}