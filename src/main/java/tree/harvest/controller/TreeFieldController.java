package tree.harvest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import tree.harvest.controller.model.ForesterData;
import tree.harvest.controller.model.TreeData;
import tree.harvest.controller.model.TreeDataOnly;
import tree.harvest.controller.model.TreeFieldData;
import tree.harvest.service.TreeFieldService;


@RestController
@RequestMapping("/the_tree_harvest")
@Slf4j
public class TreeFieldController {

	// Using Dependency Injection or "Controller Injection"
	// To give Controller Access to Service
	private TreeFieldService treeFieldService;
	
	public TreeFieldController(TreeFieldService treeFieldService) {
		this.treeFieldService = treeFieldService;
	}
	
	
	/* FORESTERS */
	
	
	/**
	 * Insert Forester in Controller Class
	 * Takes a ForesterData from the Request Body
	 * POST
	 * @param foresterData
	 * @return Service Save Forester ForesterData
	 */
	@PostMapping("/forester")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ForesterData insertForester(
			@RequestBody ForesterData foresterData) {
		log.info("Creating Forester {}", foresterData);
		return treeFieldService.saveForester(foresterData);
	}
	
	
	/**
	 * Update Forester
	 * PUT
	 * @param foresterId
	 * @param foresterData
	 * @return Saved Updated Forester
	 */
	@PutMapping("/forester/{foresterId}")
	public ForesterData updateForester(@PathVariable Long foresterId,
			@RequestBody ForesterData foresterData) {
		foresterData.setForesterId(foresterId);
		log.info("Updating Forrester {}", foresterData);
		return treeFieldService.saveForester(foresterData);
	}
	
	
	/**
	 * Retrieve All Foresters
	 * GET All Foresters
	 * @return Service of Retrieve All Foresters
	 */
	@GetMapping("/foresters")
	public List<ForesterData> retrieveAllForesters() {
		log.info("Retrieve all Foresters called.");
		return treeFieldService.retrieveAllForesters();
	}
	
	
	/**
	 * Retrieve Forester By Id
	 * GET
	 * @param foresterId
	 * @return Service
	 */
	@GetMapping("/forester/{foresterId}")
	public ForesterData retrieveForesterById(@PathVariable Long foresterId) {
		log.info("Retrieving Forrester with ID={}", foresterId);
		return treeFieldService.retrieveForesterId(foresterId);
	}
	
	
	/**
	 * Delete All Foresters
	 * DELETE
	 * Should not be implemented, therefore has a different end-point address
	 * Different URI address as a layer of protection against accidental command prompts
	 */
	@DeleteMapping("/forester/deleteAll")
	public void deleteAllForesters() {
		log.info("Attempting to delete all Foresters");
		throw new UnsupportedOperationException("Deleting all Foresters is prohibited.");		
	}
	
	
	/**
	 * Delete Forester By ID
	 * DELETE
	 * @param foresterId
	 * @return confirmation message with ID
	 */
	@DeleteMapping("/forester/{foresterId}")
	public Map<String, String> deleteForesterById(
			@PathVariable Long foresterId) {
		log.info("Deleting Forester with ID={}", foresterId);
		treeFieldService.deleteForesterById(foresterId);
		return Map.of("message", "Deletion of Forester with ID="
				+ foresterId + " was successful.");
				
	}
	
	
	/* TREE FIELDS */
	
	
	/**
	 * Insert TreeField (i.e. Park, or Forest); creates a TreeField
	 * POST
	 * @param foresterId
	 * @param treeFieldData
	 * @return saved TreeField from Service
	 */
	@PostMapping("/forester/{foresterId}/treeField")
	@ResponseStatus(code = HttpStatus.CREATED)
	public TreeFieldData insertTreeField(
			@PathVariable Long foresterId,
			@RequestBody TreeFieldData treeFieldData) {
		log.info("Creating Tree Field {} for Forester with ID={}", 
				treeFieldData,
				foresterId);
		return treeFieldService.saveTreeField(foresterId, treeFieldData);
		
	}
	
	
	/**
	 * Update TreeField (i.e. Park, or Forest) By ID
	 * PUT
	 * @param foresterId
	 * @param treeFieldId
	 * @param treeFieldData
	 * @return
	 */
	@PutMapping("/forester/{foresterId}/treeField/{treeFieldId}")
	public TreeFieldData updateTreeField(@PathVariable Long foresterId,
			@PathVariable Long treeFieldId,
			@RequestBody TreeFieldData treeFieldData) {
		treeFieldData.setTreeFieldId(treeFieldId);
		log.info("Updating Tree Field {} for Forester with ID={}", 
				treeFieldData, 
				foresterId);
		return treeFieldService.saveTreeField(foresterId, treeFieldData);
	}

	
	/**
	 * Retrieve TreeField (i.e. Park, or Forest) by ID
	 * GET
	 * @param foresterId
	 * @param treeFieldId
	 * @return
	 */
	@GetMapping("/forester/{foresterId}/treeField/{treeFieldId}")
	public TreeFieldData retrieveTreeFieldById(
			@PathVariable Long foresterId,
			@PathVariable Long treeFieldId) {
		log.info("Retrieving Tree Field with ID={} for Forester with ID={}",
				treeFieldId, 
				foresterId);
		return treeFieldService.retrieveTreeFieldById(foresterId, treeFieldId);
	}
	
	
	/**
	 * Retrieve All TreeFields (i.e. All Parks, All Forests)
	 * GET
	 * @return
	 */
	@GetMapping("/treeFields")
	public List<TreeFieldData> retrieveAllTreeFields() {
		log.info("Retrieving All Tree Fields");
		return treeFieldService.retrieveAllTreeFields();
	}
	
	
	/**
	 * Delete TreeField By ID
	 * DELETE
	 * This will delete a tree field but not delete the Forester with it.
	 * @param treeFieldId
	 * @return confirmation message with ID
	 */
	@DeleteMapping("/forester/{foresterId}/treeField/{treeFieldId}")
	public Map<String, String> deleteTreeFieldById(
			@PathVariable Long treeFieldId) {
		log.info("Deleting Tree Field with ID={}", treeFieldId);
		treeFieldService.deleteTreeFieldById(treeFieldId);
		return Map.of("message", "Deletion of Tree Field with ID="
				+ treeFieldId + " was successful.");
				
	}
	
	
	/* TREES */
	
	
	/**
	 * Retrieve All Trees (Trees and TreeFields (i.e. Forests)).
	 * GET.
	 * Pulls from Service.
	 * This retrieves all the trees and includes the tree fields associated as well.
	 * @return List of TreeData objects
	 */
	// add something for tree field
	@GetMapping("/trees")
	public List<TreeData> retrieveAllTrees() {
		log.info("Retrieve all Trees.");
		return treeFieldService.retrieveAllTrees();
	}
	
	
	/**
	 * Retrieve All The Trees (Just the Trees).
	 * GET.
	 * This retrieves only a list of all the trees in the tree table.
	 * @return All Trees
	 */
	@GetMapping("/treesOnly")
	public List<TreeDataOnly> retrieveAllTreesOnly() {
		log.info("Retrieving All Trees");
		return treeFieldService.retrieveAllTreesOnly();
	}
	
	
	/**
	 * Insert Tree.
	 * POST.
	 * Creates a Tree in the tree table.
	 * @param treeDataOnly
	 * @return a saved treeDataOnly object
	 */
	@PostMapping("/tree")
	@ResponseStatus(code = HttpStatus.CREATED)
	public TreeDataOnly insertTree(
			@RequestBody TreeDataOnly treeDataOnly) {
		log.info("Creating Tree {}", treeDataOnly);
		return treeFieldService.saveTree(treeDataOnly);
	}
	
	
	/**
	 * Delete Tree By Id.
	 * DELETE.
	 * Deletes a Tree in the tree table. (Removes tree row).
	 * CURRENTLY ONLY WORKS IF TREE IS NOT IN tree_field_tree TABLE
	 * In other words, you CANNOT delete a tree that is in a TreeField@
	 * @param treeId
	 * @return nothing, deletes a tree.
	 */
	@DeleteMapping("/deleteTree/{treeId}")
	public Map<String, String> deleteTreeById(
			@PathVariable Long treeId) {
		log.info("Deleting Tree with ID={}", treeId);
		treeFieldService.deleteTreeById(treeId);
		return Map.of("message", "Deletion of Tree with ID="
				+ treeId + " was successful.");
				
	}
	
	
}
