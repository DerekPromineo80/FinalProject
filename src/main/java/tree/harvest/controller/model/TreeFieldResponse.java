package tree.harvest.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import tree.harvest.entity.GeoLocation;
import tree.harvest.entity.Tree;
import tree.harvest.entity.TreeField;

@Data
@NoArgsConstructor
public class TreeFieldResponse {
	
	private Long treeFieldId;
	private String treeFieldName;				
	private String treeFieldDescription;	
	private String treeFieldCity;
	private String treeFieldState;	
	private String treeFieldCountry;			
	private GeoLocation fieldGeoLocation;	
	private Set<String> trees = new HashSet<>();
	
	/**
	 * Constructor
	 * @param treeField
	 */
	TreeFieldResponse(TreeField treeField) {
		treeFieldId = treeField.getTreeFieldId();
		treeFieldName = treeField.getTreeFieldName();
		treeFieldDescription = treeField.getTreeFieldDescription();
		treeFieldCity = treeField.getTreeFieldCity();
		treeFieldState = treeField.getTreeFieldState();
		treeFieldCountry = treeField.getTreeFieldCountry();
		fieldGeoLocation = treeField.getFieldGeoLocation();
		
		for (Tree tree : treeField.getTrees()) {
			trees.add(tree.getTreeName());
		}
	}
	
	
}
