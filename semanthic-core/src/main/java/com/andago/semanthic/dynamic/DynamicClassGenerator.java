package com.andago.semanthic.dynamic;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;

public class DynamicClassGenerator {
	
	private static Map<String, BasicDynaClass> dynamicClassMap;
	
	public DynamicClassGenerator() {
		dynamicClassMap = new HashMap<String,BasicDynaClass>();
	}
	
	public BasicDynaClass createDynamicClass(String className, 
			Map<String,Class> properties) {
		 List<DynaProperty> props = new ArrayList<DynaProperty>();
		 DynaProperty dynaProperty;
		 for(String propertyName : properties.keySet()) {
			 dynaProperty = new DynaProperty(propertyName, properties.get(propertyName));
			 props.add(dynaProperty);
		 }
		 DynaProperty[] aDyna = new DynaProperty[props.size()];
		 aDyna = props.toArray(aDyna);
		 BasicDynaClass dynaClass = new BasicDynaClass(className, null,aDyna); 
		 dynamicClassMap.put(className, dynaClass);
		 return dynaClass;
	}
	
	public DynaBean instanceBean(String dynaClassName, 
			Map<String,Object> values) throws Exception {
		BasicDynaClass dynaClass = this.dynamicClassMap.get(dynaClassName);
		DynaBean instance = dynaClass.newInstance();
		for(String property : values.keySet()) {
			instance.set(property, values.get(property));
		}
		return instance;
	}
	
}