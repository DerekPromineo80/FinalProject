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
public class TreeFieldDataOnly {

	private Long treeFieldId;
	private String treeFieldName;			
	private String treeFieldDescription;
	private String treeFieldCity;
	private String treeFieldState;	
	private String treeFieldCountry;		
	private GeoLocation fieldGeoLocation;
	private Set<String> trees = new HashSet<>();
	
	public TreeFieldDataOnly(TreeField treeField) {
		treeFieldId = treeField.getTreeFieldId();
		treeFieldName = treeField.getTreeFieldName();
		treeFieldDescription = treeField.getTreeFieldDescription();
		treeFieldCity = treeField.getTreeFieldCity();
		treeFieldState = treeField.getTreeFieldState();
		treeFieldCountry = treeField.getTreeFieldCountry();
		fieldGeoLocation = treeField.getFieldGeoLocation();
				
		// Cycles through trees and returns them all:
		for(Tree tree : treeField.getTrees()) {
			trees.add(tree.getTreeName());
		}
		
	}
	
}
