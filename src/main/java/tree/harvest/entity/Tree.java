package tree.harvest.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Tree {

	/**
	 * Tree is the Entity Class, creating Tree objects
	 * 
	 * Notice that treeFields is a Set
	 * It's ManyToMany
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long treeId;
	
	private String treeName;
	private String treeBinomial;
	private String lumberType;
	
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "trees")
	private Set<TreeField> treeFields = new HashSet<>();
	
	
}
