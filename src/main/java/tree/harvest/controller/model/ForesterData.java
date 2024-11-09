package tree.harvest.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import tree.harvest.entity.Forester;
import tree.harvest.entity.TreeField;

@Data
@NoArgsConstructor
public class ForesterData {

	private Long foresterId;
	private String foresterFirstName;
	private String foresterLastName;
	private String foresterEmail;
	private Set<TreeFieldResponse> treeFields = new HashSet<>();
	
	/**
	 * Constructor, because foresters have many tree fields
	 * @param forester
	 */
	public ForesterData(Forester forester) {
		foresterId = forester.getForesterId();
		foresterFirstName = forester.getForesterFirstName();
		foresterLastName = forester.getForesterLastName();
		foresterEmail = forester.getForesterEmail();
		
		for(TreeField treeField : forester.getTreeFields()) {
			treeFields.add(new TreeFieldResponse(treeField));
		}
	}
}
