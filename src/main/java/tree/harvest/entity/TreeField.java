package tree.harvest.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class TreeField {		

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long treeFieldId;
	
	private String treeFieldName;				
	private String treeFieldDescription;	
	private String treeFieldCity;
	private String treeFieldState;	
	private String treeFieldCountry;			
	
	@Embedded
	private GeoLocation fieldGeoLocation;
	
	// This is the other side of the relationship with treeFields (Entity)
	// This is the "Owned" side
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "forester_id", nullable = false)
	private Forester forester;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "tree_field_tree",
			joinColumns = @JoinColumn(name = "tree_field_id"),
			inverseJoinColumns = @JoinColumn(name = "tree_id"))
	private Set<Tree> trees = new HashSet<>();
	
}
