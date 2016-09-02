/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.PropertyUtils;
import org.devgateway.ocds.persistence.mongo.Identifiable;
import org.devgateway.ocds.persistence.mongo.Record;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.merge.Merge;
import org.devgateway.ocds.persistence.mongo.merge.MergeStrategy;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

/**
 * @author mihai
 * 
 */
@Service
public class ReleaseCompilerService {

	protected static final Logger logger = LoggerFactory.getLogger(ReleaseCompilerService.class);

//	@Autowired
//	private OcdsSchemaValidatorService ocdsSchemaValidatorService;
//
//	@Autowired
//	private ObjectMapper jacksonObjectMapper;

	@Autowired
	protected Reflections reflections;

	private Set<Field> fieldsAnnotatedWithMerge;

	@PostConstruct
	protected void init() {
		fieldsAnnotatedWithMerge = Sets.newConcurrentHashSet(reflections.getFieldsAnnotatedWith(Merge.class));
	}

	public Object mergeFieldStrategyOverwrite(Object left, Object right) {
		left = right;
		return left;
	}
	
	public Identifiable getIdentifiableById(Serializable id, Collection<Identifiable> col) {
		for (Identifiable identifiable : col) {
			if (identifiable.getId().equals(id)) {
				return identifiable;
			}
		}
		return null;
	}
	
	public <S extends Collection<Identifiable>> S mergeFieldStrategyArrayMergeById(S left, S right) {
		// iterate all right elements
		for (Identifiable identifiable : right) {

			// if there is an existing element with the same id, perform merge
			// on the children and replace existing left element
			Identifiable leftIdentifiable = getIdentifiableById(identifiable.getId(), left);
			if (leftIdentifiable != null) {
				left.remove(leftIdentifiable);
				left.add(createCompiledObject(leftIdentifiable, identifiable));
			} else {
				//otherwise add the new element to the left list
				left.add(identifiable);
			}
		}
		return left;
	}

	/**
	 * Merges the right release into the left. This will mutate the left release
	 * and return it. Invoking this on the toplevel {@link Release} entities,
	 * will create a compiledRelease
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public <S> S createCompiledObject(S left, S right) {

		Class<?> clazz = right.getClass();

		if (!left.getClass().equals(clazz)) {
			throw new RuntimeException("Attempted the merging of objects of different type!");
		}

		Field[] rightDeclaredFields = right.getClass().getDeclaredFields();

		Field field = null;
		for (int i = 0; i < rightDeclaredFields.length; i++) {
			try {
				field = rightDeclaredFields[i];
				String fieldName = rightDeclaredFields[i].getName();
				Object rightFieldValue = PropertyUtils.getProperty(right, fieldName);
				Object leftFieldValue = PropertyUtils.getProperty(left, fieldName);
				if (fieldsAnnotatedWithMerge.contains(field)) {
					MergeStrategy mergeStrategy = field.getDeclaredAnnotation(Merge.class).value();
					switch (mergeStrategy) {
					case overwrite:
						PropertyUtils.setProperty(left, fieldName,
								mergeFieldStrategyOverwrite(leftFieldValue, rightFieldValue));
						break;
					case ocdsOmit:
						PropertyUtils.setProperty(left, fieldName, null);
					case ocdsVersion:
						PropertyUtils.setProperty(left, fieldName,
								mergeFieldStrategyOverwrite(leftFieldValue, rightFieldValue));
					case arrayMergeById: 
						
					default:
						throw new RuntimeException("Unknown merge strategy!");
					}
				} else {
					PropertyUtils.setProperty(left, fieldName, createCompiledObject(leftFieldValue, rightFieldValue));
				}

			} catch (Exception e) {
				logger.error(
						e.getMessage() + " while processing field " + clazz.getSimpleName() + "." + field.getName());
				throw new RuntimeException(e);
			}
		}

		return left;

	}

	public Release createCompiledRelease(Record record) {
		return null;
	}
}
