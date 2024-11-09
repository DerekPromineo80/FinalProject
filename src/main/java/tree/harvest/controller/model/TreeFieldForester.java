package tree.harvest.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import tree.harvest.entity.Forester;

@Data
@NoArgsConstructor
public class TreeFieldForester {
	
	private Long foresterId;
	private String foresterFirstName;
	private String foresterLastName;
	private String foresterEmail;

	public TreeFieldForester(Forester forester) {
		foresterId = forester.getForesterId();
		foresterFirstName = forester.getForesterFirstName();
		foresterLastName = forester.getForesterLastName();
		foresterEmail = forester.getForesterEmail();
		
	}
}
