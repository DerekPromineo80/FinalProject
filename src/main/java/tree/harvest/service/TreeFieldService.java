package tree.harvest.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tree.harvest.controller.model.ForesterData;
import tree.harvest.controller.model.TreeData;
import tree.harvest.controller.model.TreeDataOnly;
import tree.harvest.controller.model.TreeFieldData;
import tree.harvest.dao.ForesterDao;
import tree.harvest.dao.TreeDao;
import tree.harvest.dao.TreeFieldDao;
import tree.harvest.entity.Forester;
import tree.harvest.entity.Tree;
import tree.harvest.entity.TreeField;

/* SERVICE CLASS */

@Service
public class TreeFieldService {

	private ForesterDao foresterDao;
	private TreeDao treeDao;
	private TreeFieldDao treeFieldDao;
	
	// Using Dependency Injection or "Constructor Injection"
	// To give Service access to DAO layer
	// instead of @Autowired
	// https://odrotbohm.de/2013/11/why-field-injection-is-evil/
	public TreeFieldService(ForesterDao foresterDao,
			TreeDao treeDao, TreeFieldDao treeFieldDao) {
		this.foresterDao = foresterDao;
		this.treeDao = treeDao;
		this.treeFieldDao = treeFieldDao;
	}
	
	/* FORESTERS */
	
	
	/**
	 * 1 Save Forester.
	 * Service Class.
	 * Writes a JSON body into a Forester row.
	 * @param foresterData
	 * @return a ForesterData object that is a saved forester from the DAO layer
	 */
	@Transactional(readOnly = false)
	public ForesterData saveForester(ForesterData foresterData) {
		Long forresterId = foresterData.getForesterId();
		Forester forester = findOrCreateForester(forresterId,
				foresterData.getForesterEmail());
		
		setFieldsInForester(forester, foresterData);
		
		return new ForesterData(foresterDao.save(forester));
	}
	
	
	/**
	 * 2 Set Fields In Forester.
	 * Service Class.
	 * Helper Method.
	 * Used in the Save Forester Method in TreeFieldController.
	 * @param forester
	 * @param foresterData
	 */
	private void setFieldsInForester(Forester forester,
			ForesterData foresterData) {
		forester.setForesterEmail(foresterData.getForesterEmail());
		forester.setForesterFirstName(foresterData.getForesterFirstName());
		forester.setForesterLastName(foresterData.getForesterLastName());
	}
	
	
	/**
	 * 3. Find Or Create Forester.
	 * Service Class.
	 * Helper Method.
	 * Used in Save Forester Method in TreeFieldController.
	 * @param foresterId
	 * @param foresterEmail
	 * @return Forester object called forester
	 */
	private Forester findOrCreateForester(Long foresterId,
			String foresterEmail) {
		Forester forester;
		if(Objects.isNull(foresterId)) {
			Optional<Forester> opForrester = 
					foresterDao.findByForesterEmail(foresterEmail);
			if(opForrester.isPresent()) {
				throw new DuplicateKeyException (
					"Forester with email " + foresterEmail 
					+ " already exists."); 
			}
			forester = new Forester();
		}
		else {
			forester = findForesterById(foresterId);
		}
		return forester;
	}
	
	
	/**
	 * 4. Find Forester By Id.
	 * Service Class.
	 * @param foresterId
	 * @return Forester, if ID exists. Otherwise throws exception.
	 */
	private Forester findForesterById(Long foresterId) {
		return foresterDao.findById(foresterId)
				.orElseThrow(() -> new NoSuchElementException(
						"Forester with ID=" + foresterId
						+ " was not found."));
	}

	
	/**
	 * 5. Retrieve All Foresters.
	 * Service Class.
	 * @return ForesterData List
	 */
	@Transactional(readOnly = true)
	public List<ForesterData> retrieveAllForesters() {
		List<Forester> foresters = foresterDao.findAll();
		List<ForesterData> response = new LinkedList<>();
		for (Forester forester : foresters) {
			response.add(new ForesterData(forester));
		}
		return response;
	}
	
	
	/**
	 * 6. Retrieve Forester Id.
	 * Service Class.
	 * @param foresterId
	 * @return ForesterData of a new Forester Object
	 */
	@Transactional(readOnly = true)
	public ForesterData retrieveForesterId(Long foresterId) {
		Forester forester = findForesterById(foresterId);
		return new ForesterData(forester);
	}
	
	
	/**
	 * 7. Delete Forester By Id.
	 * Service Class.
	 * @param foresterId
	 */
	@Transactional(readOnly = false)
	public void deleteForesterById(Long foresterId) {
		Forester forester = findForesterById(foresterId);
		foresterDao.delete(forester);
	}

	
	/* TREE FIELDS */
	
	
	/**
	 * 8. Save Tree Field.
	 * Service Class.
	 * Used in PUT operation in Controller.
	 * @param foresterId
	 * @param treeFieldData
	 * @return TreeField Data
	 */
	@Transactional(readOnly = false)
	public TreeFieldData saveTreeField(Long foresterId,
			TreeFieldData treeFieldData) {
		Forester forester = findForesterById(foresterId);

		Set<Tree> trees = treeDao.findAllByTreeNameIn(treeFieldData.getTrees());
		
		TreeField treeField = findOrCreateTreeField(treeFieldData.getTreeFieldId());
		setTreeFieldFields(treeField, treeFieldData);
		
		treeField.setForester(forester);
		forester.getTreeFields().add(treeField);
		
		for(Tree tree : trees) {					
			tree.getTreeFields().add(treeField);
			treeField.getTrees().add(tree);
		}
		
		TreeField dbTreeField = treeFieldDao.save(treeField);
		return new TreeFieldData(dbTreeField);
		
	}
	
	
	/**
	 * 9. Set TreeField Fields.
	 * Service Class Helper Method.
	 * Sets the fields in a treeField by getting the data from a treefieldData.
	 * Used in the method saveTreeField in Service.
	 * @param treeField
	 * @param treeFieldData filled in with new data
	 */
	private void setTreeFieldFields(TreeField treeField, TreeFieldData treeFieldData) {
		treeField.setTreeFieldCountry(treeFieldData.getTreeFieldCountry());
		treeField.setTreeFieldDescription(treeFieldData.getTreeFieldDescription());
		treeField.setFieldGeoLocation(treeFieldData.getFieldGeoLocation());
		treeField.setTreeFieldName(treeFieldData.getTreeFieldName());
		treeField.setTreeFieldId(treeFieldData.getTreeFieldId());
		treeField.setTreeFieldCity(treeFieldData.getTreeFieldCity());
		treeField.setTreeFieldState(treeFieldData.getTreeFieldState());
	}

	
	/**
	 * 10. Find or Create Tree Field.
	 * Service Class.
	 * If treeFieldId is null, creates treeField.
	 * If treeFieldId exists, finds treeField.
	 * @param treeFieldId
	 * @return TreeField object, either existing or newly created.
	 */
	private TreeField findOrCreateTreeField(Long treeFieldId) {
		TreeField treeField;
		
		if(Objects.isNull(treeFieldId)) {
			treeField = new TreeField();
		}	
		else {
			treeField = findTreeFieldById(treeFieldId);
		}
		
		return treeField;
	}
	
	
	/**
	 * 11. Find Tree Field By Id.
	 * Service Class.
	 * If no treeFieldId exists,
	 * Throws exception.
	 * @param treeFieldId
	 * @return TreeField found by ID
	 */
	private TreeField findTreeFieldById(Long treeFieldId) {
		return treeFieldDao.findById(treeFieldId)
				.orElseThrow(() -> new NoSuchElementException(
						"Tree Field with ID=" 
						+ treeFieldId + 
						"does not exist."));
	}
	
	
	/**
	 * 12. Retrieve Tree Field by Id.
	 * Service Class.
	 * Requires the correct Forester ID, 
	 * Otherwise throws exception.
	 * @param foresterId
	 * @param treeFieldId
	 * @return new TreeFieldData object
	 */
	@Transactional(readOnly = true)
	public TreeFieldData retrieveTreeFieldById(Long foresterId, Long treeFieldId) {
		findForesterById(foresterId);
		TreeField treeField = findTreeFieldById(treeFieldId);
		
		if(treeField.getForester().getForesterId() != foresterId) {
			throw new IllegalStateException("Tree Field with ID=" + treeFieldId 
					+ " is not owned by Forester with ID="
					+ foresterId);
		}
		
		return new TreeFieldData(treeField);
		
	}


	/**
	 * 13. Retrieve All Tree Fields.
	 * Service Class.
	 * Used in "/the_tree_harvest/treeFields".
	 * Creates a List of Tree Fields, from DAO.
	 * Creates a TreeFieldData List,
	 * For each Tree field in the treeFields list.
	 * Adds a treeField data to that list.
	 * @return List of TreeFieldData with updated treeFields
	 */
	@Transactional(readOnly = true)
	public List<TreeFieldData> retrieveAllTreeFields() {
		List<TreeField> treeFields = treeFieldDao.findAll();
		List<TreeFieldData> response = new LinkedList<>();
		for (TreeField treefield : treeFields) {
			response.add(new TreeFieldData(treefield));
		}
		return response;
	}
	
	
	/**
	 * 14. Delete Tree Field By Id.
	 * Service Class.
	 * Uses treeFieldDAO to delete.
	 * @param foresterId
	 * @return nothing, it removes the TreeField row
	 */
	@Transactional(readOnly = false)
	public void deleteTreeFieldById(Long treeFieldId) {
		TreeField treeField = findTreeFieldById(treeFieldId);
		treeField.setForester(null);
		treeFieldDao.delete(treeField);
	}
	
	
	/* TREES */
	
	
	/**
	 * 15. Retrieve All Trees
	 * Service Class.
	 * Uses treeDao to find a List of Trees.
	 * Sends to Controller for "/the_tree_harvest/trees".
	 * @return Tree Data List
	 */
	@Transactional(readOnly = true)
	public List<TreeData> retrieveAllTrees() {
		List<Tree> trees = treeDao.findAll();
		List<TreeData> response = new LinkedList<>();
		for (Tree tree : trees) {
			response.add(new TreeData(tree));
		}
		return response;
	}

	
	/**
	 * 16. Retrieve Trees Only.
	 * Service Class.
	 * Sends to Controller for "/the_tree_harvest/treesOnly".
	 * @return List of TreeDataOnly with Trees added to it
	 */
	@Transactional(readOnly = true)
	public List<TreeDataOnly> retrieveAllTreesOnly() {
		List<Tree> trees = treeDao.findAll();
		List<TreeDataOnly> response = new LinkedList<>();
		for (Tree tree : trees) {
			response.add(new TreeDataOnly(tree));
		}
		return response;
	}
	
	
	/**
	 * 17. Save Tree.
	 * Service Class.
	 * Sends to Controller for POST for "/the_tree_harvest/tree".
	 * @param treeDataOnly
	 * @return Tree Data Only object from TreeDao
	 */
	@Transactional(readOnly = false)
	public TreeDataOnly saveTree(TreeDataOnly treeDataOnly) {
		Long treeId = treeDataOnly.getTreeId();
		Tree tree = findOrCreateTree(treeId,
				treeDataOnly.getTreeName());
		
		setFieldsInTree(tree, treeDataOnly);
		
		return new TreeDataOnly(treeDao.save(tree));
	}

	
	/**
	 * 18. Set Fields In Tree.
	 * Service Class.
	 * Helper Method.
	 * Used with saveTree.
	 * @param tree
	 * @param treeDataOnly
	 */
	private void setFieldsInTree(Tree tree, TreeDataOnly treeDataOnly) {
		tree.setTreeName(treeDataOnly.getTreeName());
		tree.setLumberType(treeDataOnly.getLumberType());
		tree.setTreeBinomial(treeDataOnly.getTreeBinomial());
	}

	
	/**
	 * 19. Find Or Create Tree.
	 * Service Class.
	 * Helper Method.
	 * Used with saveTree.
	 * @param treeId
	 * @param treeName
	 * @return tree object
	 */
	private Tree findOrCreateTree(Long treeId, String treeName) {
		Tree tree;
		if(Objects.isNull(treeId)) {
			Optional<Tree> opTree = 
					treeDao.findByTreeName(treeName);
			if(opTree.isPresent()) {
				throw new DuplicateKeyException (
					"Tree with name " + treeName 
					+ " already exists."); 
			}
			tree = new Tree();
		}
		else {
			tree = findTreeByName(treeName);
		}
		return tree;
	}

	
	/**
	 * 20. Find Tree By Name.
	 * Service Class.
	 * Helper Method.
	 * Used with findOrCreateTree.
	 * @param treeName
	 * @return Tree, by Name
	 */
	private Tree findTreeByName(String treeName) {
		return treeDao.findByTreeName(treeName)
				.orElseThrow(() -> new NoSuchElementException(
						"Tree with name="
						+ treeName 
						+ " was not found."));
	}
	
	
	/**
	 * 21. Delete Tree By Id.
	 * Service Class.
	 * Deletes a Tree from the tree table.
	 * @param treeId
	 */
	@Transactional(readOnly = false)
	public void deleteTreeById(Long treeId) {
		//treeDao.deleteByTreeIdInJoin(treeId);
		Tree tree = findTreeById(treeId);
		treeDao.delete(tree);
	}
	
	
	/**
	 * 22. Find Tree By Id.
	 * Service Class.
	 * Helper Method.
	 * Used with deleteTreeById.
	 * @param treeId
	 * @return Tree object, by Id
	 */
	private Tree findTreeById(Long treeId) {
		return treeDao.findById(treeId)
				.orElseThrow(()-> new NoSuchElementException(
						"Tree with ID="
						+ treeId
						+ " was not found."));
	}
	
	
}



