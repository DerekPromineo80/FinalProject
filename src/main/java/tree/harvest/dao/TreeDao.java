package tree.harvest.dao;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import tree.harvest.entity.Tree;

public interface TreeDao extends JpaRepository<Tree, Long> {

	/**
	 * Used in saveTreeField method, Service class
	 * @param trees
	 * @return Set of Trees
	 */
	Set<Tree> findAllByTreeNameIn(Set<String> trees);

	
	/**
	 * Used to create New Tree
	 * Used in findTreeByName method, Service class
	 * POST
	 * @param treeName
	 * @return Tree
	 */
	Optional<Tree> findByTreeName(String treeName);
	
	
}
