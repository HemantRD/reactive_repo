package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.Subcategory;

public interface ISubCategoryDao extends JpaRepository<Subcategory, Long>{

	@Query(value = "select * from tbl_mst_kra_sub_category where mst_category_id=?1 and is_active=?2 ", nativeQuery = true)
	public List<Subcategory> findBySubCategoryId(Long categoryId,String isActive);
	
	Subcategory findBySubCategoryNameAndCategoryAndIsActive(String subCategoryName, Category category, String isActive);

	Subcategory findByCategoryAndIsActive(Category category, String name);	
	
	
	Subcategory findByCategoryId(long category);

	Subcategory findByIdAndIsActive(Long subCategoryId, String isactive);

	public List<Subcategory> findAllByIsActive(String name);

	List<Subcategory> findAllByCategory_IdAndIsActive(Long categoryId, String isActive);

	

}
