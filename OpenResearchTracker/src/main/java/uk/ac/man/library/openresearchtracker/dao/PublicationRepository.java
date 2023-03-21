package uk.ac.man.library.openresearchtracker.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.ac.man.library.openresearchtracker.entities.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,String>{
	
	@Query("SELECT DISTINCT p FROM Publication p " 
			+ "LEFT JOIN p.faculties f " 
			+ "LEFT JOIN p.schools s " 
			+ "WHERE "
			+ "(:title='' OR lower(p.title) LIKE lower(concat('%',:title,'%'))) "
			+ "AND (:pureStatus='' OR p.pure_status IN :pureStatus) "
			+ "AND (:complianceStatus='' OR p.compliance_status IN :complianceStatus) "
			+ "AND (:oa_route='' OR p.oa_route IN :oa_route) "
			+ "AND ("
			+ "(:funder_Wellcome='' AND :funder_CRUK='' AND :funder_BHF='' "
			+ "AND :funder_NIHR='' AND :funder_ERC='' AND :funder_UKRI='') "
			+ "OR"
			+ "(p.funder_Wellcome = :funder_Wellcome "
			+ "OR p.funder_CRUK = :funder_CRUK "
			+ "OR p.funder_BHF = :funder_BHF "
			+ "OR p.funder_NIHR = :funder_NIHR "
			+ "OR p.funder_ERC = :funder_ERC "
			+ "OR p.funder_UKRI = :funder_UKRI)) "
			
			+ "AND (:schoolName='' OR s.school_name IN :schoolName) "
			+ "AND (:facultyName='' OR f.faculty_name IN :facultyName)"
			)
	Page<Publication> publicationFilter(@Param("title")String title, 
			@Param("pureStatus")String pureStatus, 
			@Param("complianceStatus")String complianceStatus, 
			@Param("oa_route")String oa_route, 
			@Param("funder_Wellcome")String funder_Wellcome, 
			@Param("funder_CRUK")String funder_CRUK, 
			@Param("funder_BHF")String funder_BHF, 
			@Param("funder_NIHR")String funder_NIHR, 
			@Param("funder_ERC")String funder_ERC, 
			@Param("funder_UKRI")String funder_UKRI, 
			@Param("facultyName")String facultyName, 
			@Param("schoolName")String schoolName, 
			Pageable pageable);
	
	@Query("SELECT DISTINCT p FROM Publication p " 
			+ "LEFT JOIN p.faculties f " 
			+ "LEFT JOIN p.schools s " 
			+ "WHERE "
			+ "(:title='' OR lower(p.title) LIKE lower(concat('%',:title,'%'))) "
			+ "AND (:pureStatus='' OR p.pure_status IN :pureStatus) "
			+ "AND (:complianceStatus='' OR p.compliance_status IN :complianceStatus) "
			+ "AND (:oa_route='' OR p.oa_route IN :oa_route) "
			+ "AND ("
			+ "(:funder_Wellcome='' AND :funder_CRUK='' AND :funder_BHF='' "
			+ "AND :funder_NIHR='' AND :funder_ERC='' AND :funder_UKRI='') "
			+ "OR"
			+ "(p.funder_Wellcome = :funder_Wellcome "
			+ "OR p.funder_CRUK = :funder_CRUK "
			+ "OR p.funder_BHF = :funder_BHF "
			+ "OR p.funder_NIHR = :funder_NIHR "
			+ "OR p.funder_ERC = :funder_ERC "
			+ "OR p.funder_UKRI = :funder_UKRI)) "
			
			+ "AND (:schoolName='' OR s.school_name IN :schoolName) "
			+ "AND (:facultyName='' OR f.faculty_name IN :facultyName)"
			)
	List<Publication> publicationFilter(@Param("title")String title, 
			@Param("pureStatus")String pureStatus, 
			@Param("complianceStatus")String complianceStatus, 
			@Param("oa_route")String oa_route, 
			@Param("funder_Wellcome")String funder_Wellcome, 
			@Param("funder_CRUK")String funder_CRUK, 
			@Param("funder_BHF")String funder_BHF, 
			@Param("funder_NIHR")String funder_NIHR, 
			@Param("funder_ERC")String funder_ERC, 
			@Param("funder_UKRI")String funder_UKRI, 
			@Param("facultyName")String facultyName, 
			@Param("schoolName")String schoolName);
	
	@Query("SELECT p FROM Publication p WHERE p.pureId = :pureId")
	Optional<Publication> findById(@Param("pureId")String pureId);
}
