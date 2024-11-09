package tree.harvest.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import tree.harvest.entity.Tree;
import tree.harvest.entity.TreeField;

@Data
@NoArgsConstructor
public class TreeResponse {

	private Long treeId;
	private String treeName;
	private String treeBinomial;
	private String lumberType;
	
	private Set<TreeFieldData> treeFields = new HashSet<>();
	
	TreeResponse(Tree tree) {
		treeId = tree.getTreeId();
		treeName = tree.getTreeName();
		treeBinomial = tree.getTreeBinomial();
		lumberType = tree.getLumberType();

		for(TreeField treeField : tree.getTreeFields()) {
			treeFields.add(new TreeFieldData(treeField));
		}
	}
}
