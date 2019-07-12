/*
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.kubernetes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kubesys.kubernetes.api.model.DoneableVirtualMachine;
import com.github.kubesys.kubernetes.api.model.DoneableVirtualMachineDisk;
import com.github.kubesys.kubernetes.api.model.DoneableVirtualMachineImage;
import com.github.kubesys.kubernetes.api.model.DoneableVirtualMachineSnapshot;
import com.github.kubesys.kubernetes.api.model.DoneableVirtualMachineUITDisk;
import com.github.kubesys.kubernetes.api.model.VirtualMachine;
import com.github.kubesys.kubernetes.api.model.VirtualMachineDisk;
import com.github.kubesys.kubernetes.api.model.VirtualMachineDiskList;
import com.github.kubesys.kubernetes.api.model.VirtualMachineImage;
import com.github.kubesys.kubernetes.api.model.VirtualMachineImageList;
import com.github.kubesys.kubernetes.api.model.VirtualMachineList;
import com.github.kubesys.kubernetes.api.model.VirtualMachineSnapshot;
import com.github.kubesys.kubernetes.api.model.VirtualMachineSnapshotList;
import com.github.kubesys.kubernetes.api.model.VirtualMachineUITDisk;
import com.github.kubesys.kubernetes.api.model.VirtualMachineUITDiskList;
import com.github.kubesys.kubernetes.impl.VirtualMachineImpl;

import io.fabric8.kubernetes.api.model.Doneable;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since 2019/5/1
 *
 *        This code is used to manage CustomResource's lifecycle, such as
 *        VirtualMachine
 */
public class ExtendedKubernetesClient extends DefaultKubernetesClient {

	public final static Logger m_logger = Logger.getLogger(ExtendedKubernetesClient.class.getName());

	public final static String TOKEN = "/etc/kubernetes/admin.conf";

	public final static String PACKAGE = ExtendedKubernetesClient.class.getPackage().getName();

	public final static String SUBPKG = ".api.model.";

	public final static Map<String, CustomResourceDefinition> crds = new HashMap<String, CustomResourceDefinition>();

	@SuppressWarnings("rawtypes")
	public final static Map<String, MixedOperation> executors = new HashMap<String, MixedOperation>();

	protected void initCustomVMResources()  {
		try {
			Properties props =  new Properties();
			props.load(getClass()
					.getResourceAsStream("/VirtualMachine.conf"));
			
			String name = props.getProperty("PLURAL") + "." + props.getProperty("GROUP");
			String kind = props.getProperty("KIND");
			String version = props.getProperty("GROUP") + "/" + props.getProperty("VERSION");

			CustomResourceDefinition crd = getCustomResourceDefinition(name);
			crds.put(kind, crd);
			KubernetesDeserializer.registerCustomKind(version, kind, getCustomResourceClass(kind));
			@SuppressWarnings("rawtypes")
			MixedOperation executor = (MixedOperation) customResources(crds.get(VirtualMachine.class.getSimpleName()),
					VirtualMachine.class, VirtualMachineList.class, DoneableVirtualMachine.class).inAnyNamespace();
			executors.put(kind, executor);
			
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Starting scheduler fail.");
		}
	}
	
	protected void initCustomVMSResources()  {
		try {
			Properties props =  new Properties();
			props.load(getClass()
					.getResourceAsStream("/VirtualMachineSnapshot.conf"));
			
			String name = props.getProperty("PLURAL") + "." + props.getProperty("GROUP");
			String kind = props.getProperty("KIND");
			String version = props.getProperty("GROUP") + "/" + props.getProperty("VERSION");

			CustomResourceDefinition crd = getCustomResourceDefinition(name);
			crds.put(kind, crd);
			KubernetesDeserializer.registerCustomKind(version, kind, getCustomResourceClass(kind));
			@SuppressWarnings("rawtypes")
			MixedOperation executor = (MixedOperation) customResources(crds.get(VirtualMachineSnapshot.class.getSimpleName()),
					VirtualMachineSnapshot.class, VirtualMachineSnapshotList.class, DoneableVirtualMachineSnapshot.class).inAnyNamespace();
			executors.put(kind, executor);
			
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Starting scheduler fail.");
		}
	}
	
	protected void initCustomVMIResources()  {
		try {
			Properties props =  new Properties();
			props.load(getClass()
					.getResourceAsStream("/VirtualMachineImage.conf"));
			
			String name = props.getProperty("PLURAL") + "." + props.getProperty("GROUP");
			String kind = props.getProperty("KIND");
			String version = props.getProperty("GROUP") + "/" + props.getProperty("VERSION");

			CustomResourceDefinition crd = getCustomResourceDefinition(name);
			crds.put(kind, crd);
			KubernetesDeserializer.registerCustomKind(version, kind, getCustomResourceClass(kind));
			@SuppressWarnings("rawtypes")
			MixedOperation executor = (MixedOperation) customResources(crds.get(VirtualMachineImage.class.getSimpleName()),
					VirtualMachineImage.class, VirtualMachineImageList.class, DoneableVirtualMachineImage.class).inAnyNamespace();
			executors.put(kind, executor);
			
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Starting scheduler fail.");
		}
	}
	
	protected void initCustomVMDResources()  {
		try {
			Properties props =  new Properties();
			props.load(getClass()
					.getResourceAsStream("/VirtualMachineDisk.conf"));
			
			String name = props.getProperty("PLURAL") + "." + props.getProperty("GROUP");
			String kind = props.getProperty("KIND");
			String version = props.getProperty("GROUP") + "/" + props.getProperty("VERSION");

			CustomResourceDefinition crd = getCustomResourceDefinition(name);
			crds.put(kind, crd);
			KubernetesDeserializer.registerCustomKind(version, kind, getCustomResourceClass(kind));
			@SuppressWarnings("rawtypes")
			MixedOperation executor = (MixedOperation) customResources(crds.get(VirtualMachineDisk.class.getSimpleName()),
					VirtualMachineDisk.class, VirtualMachineDiskList.class, DoneableVirtualMachineDisk.class).inAnyNamespace();
			executors.put(kind, executor);
			
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Starting scheduler fail.");
		}
	}
	
	protected void initCustomVMUDResources()  {
		try {
			Properties props =  new Properties();
			props.load(getClass()
					.getResourceAsStream("/VirtualMachineUITDisk.conf"));
			
			String name = props.getProperty("PLURAL") + "." + props.getProperty("GROUP");
			String kind = props.getProperty("KIND");
			String version = props.getProperty("GROUP") + "/" + props.getProperty("VERSION");

			CustomResourceDefinition crd = getCustomResourceDefinition(name);
			crds.put(kind, crd);
			KubernetesDeserializer.registerCustomKind(version, kind, getCustomResourceClass(kind));
			@SuppressWarnings("rawtypes")
			MixedOperation executor = (MixedOperation) customResources(crds.get(VirtualMachineUITDisk.class.getSimpleName()),
					VirtualMachineUITDisk.class, VirtualMachineUITDiskList.class, DoneableVirtualMachineUITDisk.class).inAnyNamespace();
			executors.put(kind, executor);
			
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Starting scheduler fail.");
		}
	}

	public ExtendedKubernetesClient(Config config) throws Exception {
		super(config);
		initCustomVMResources();
		initCustomVMIResources();
		initCustomVMDResources();
		initCustomVMUDResources();
		initCustomVMSResources();
	}

	/**
	 * @param conf file name
	 * @return key and values
	 * @throws Exception the file does not exist
	 */
	protected Properties loadConfig(File conf) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream(conf));
		return props;
	}

	/**
	 * @param crd              crd
	 * @param resourceType     resource type
	 * @param resourceList     resource list
	 * @param doneableRespurce doneable resource
	 * @return custom resource
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected MixedOperation getCustomResource(CustomResourceDefinition crd, Class<? extends HasMetadata> resourceType,
			Class<? extends KubernetesResourceList> resourceList, Class<? extends Doneable> doneableRespurce) {
		return this.customResources(crd, resourceType, resourceList, doneableRespurce);
	}

	/**
	 * @param kind resource type
	 * @return resource class if the class exist
	 * @throws Exception class not found
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class<? extends KubernetesResource> getCustomResourceClass(String kind) throws Exception {
		return (Class<? extends KubernetesResource>) Class.forName(PACKAGE + SUBPKG + kind);
	}

	/**
	 * @param name resource name
	 * @return the related CustomResourceDefinition
	 */
	protected CustomResourceDefinition getCustomResourceDefinition(String name) {
		return this.customResourceDefinitions().withName(name).get();
	}

	/**
	 * @return this files located at 'src/main/resources'
	 */
	protected File[] getConfigs() {
		File res = new File(this.getClass().getClassLoader().getResource("").getFile());
		return res.listFiles();
	}

	/**
	 * @param watcher watcher
	 */
	@SuppressWarnings("unchecked")
	public void watchVirtualMachine(Watcher<VirtualMachine> watcher) {
		try {
			executors.get(VirtualMachine.class.getSimpleName()).watch(watcher);
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Fail to start.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void watchVirtualMachineImage(Watcher<VirtualMachineImage> watcher) {
		try {
			executors.get(VirtualMachineImage.class.getSimpleName()).watch(watcher);
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Fail to start.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void watchVirtualMachineDisk(Watcher<VirtualMachineDisk> watcher) {
		try {
			executors.get(VirtualMachineDisk.class.getSimpleName()).watch(watcher);
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Fail to start.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void watchVirtualMachineUITDisk(Watcher<VirtualMachineUITDisk> watcher) {
		try {
			executors.get(VirtualMachineUITDisk.class.getSimpleName()).watch(watcher);
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Fail to start.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void watchVirtualMachineSnapshot(Watcher<VirtualMachineSnapshot> watcher) {
		try {
			executors.get(VirtualMachineSnapshot.class.getSimpleName()).watch(watcher);
		} catch (Exception e) {
			m_logger.log(Level.SEVERE, "Fail to start.");
		}
	}

	/**
	 * @param config config
	 * @return ExtendedKubernetesClient
	 * @throws Exception unable to init
	 */
	public static ExtendedKubernetesClient defaultConfig(String config) throws Exception {
		return new ExtendedKubernetesClient(Serialization.unmarshal(config, Config.class));
	}

	/**
	 * @param is file
	 * @return ExtendedKubernetesClient
	 * @throws Exception unable to init
	 */
	public static ExtendedKubernetesClient defaultConfig(InputStream is) throws Exception {
		return new ExtendedKubernetesClient(Serialization.unmarshal(is, Config.class));
	}
	
	public VirtualMachineImpl virtualMachines() {
		return new VirtualMachineImpl(executors.get(VirtualMachine.class.getSimpleName()));
	}

}