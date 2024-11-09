package tree.harvest.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import tree.harvest.entity.Tree;

@Data
@NoArgsConstructor
public class TreeDataOnly {

	private Long treeId;
	private String treeName;
	private String treeBinomial;
	private String lumberType;
	
	public TreeDataOnly(Tree tree) {
		treeId = tree.getTreeId();
		treeName = tree.getTreeName();
		treeBinomial = tree.getTreeBinomial();
		lumberType = tree.getLumberType();
				
	}
}
