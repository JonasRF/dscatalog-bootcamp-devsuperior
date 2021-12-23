import { ReactComponent as SearchIcon } from "assets/images/SearchIcon.svg";
import { useEffect, useState } from "react";
import { Controller, useForm } from "react-hook-form";
import Select from "react-select";
import { Category } from "types/category";
import { requestBackend } from "util/requests";

import "./styles.css";

type ProductFilterData = {
  name: string;
  category: Category;
};

const ProductFilter = () => {

  const [selectCategories, setSelectCAtegories] = useState<Category[]>([]);

  const {
    register,
    handleSubmit,
    control,
  } = useForm<ProductFilterData>();

  const onSubmit = (formData: ProductFilterData) => {
    console.log("Sucesso", formData);
  };

  useEffect(() => {
    requestBackend({ url: "/categories" }).then((response) => {
      setSelectCAtegories(response.data.content);
    });
  });

  return (
    <div className="base-card product-filter-container">
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="product-filter-container"
      >
        <div className="product-filter-name-container">
          <input
            {...register("name")}
            type="text"
            className="form-control"
            placeholder="Nome do produto"
            name="name"
          />
          <button className="Product-filter-button-search-icon">
            <SearchIcon />
          </button>
        </div>
        <div className="product-filter-botton-container">
          <div className="product-filter-category-container">
            <Controller
              name="category"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  options={selectCategories}
                  isClearable
                  placeholder="Categoria"
                  classNamePrefix="product-filter-select"
                  getOptionLabel={(category: Category) => category.name}
                  getOptionValue={(category: Category) => String(category.id)}
                />
              )}
            />
          </div>
          <button className="btn btn-outline-secondary btn-product-filter-clear">LIMPAR <span className="btn-product-filter-word">FILTRO</span></button>
        </div>
      </form>
    </div>
  );
};

export default ProductFilter;
