import ProductCard from "components/ProductCard";
import { Product } from "types/product";
import { Link } from "react-router-dom";
import Pagination from "components/Pagination";
import { useState, useEffect, useCallback } from "react";
import { SpringPage } from "types/vendor/spring";
import { requestBackend } from "util/requests";
import { AxiosRequestConfig } from "axios";
import CardLoader from "./CardLoader";

import "./styles.css";

type ControlComponentsData = {
  activePage: number;
}

const Catalog = () => {
  const [page, setPage] = useState<SpringPage<Product>>();
  const [isLoading, setIsLoading] = useState(false);

  //Controle de componentes
  const [ controlComponentesData, setControlComponentsData ] = useState<ControlComponentsData>({

    activePage: 0,

   }
  );

  const handlePageChange = (pageNamber: number) => {
    setControlComponentsData({activePage: pageNamber});
  }

const getProducts = useCallback(() => {
  const params: AxiosRequestConfig = {
    method: "GET",
    url: "products",
    params: {
      page: controlComponentesData.activePage,
      size: 12,
    }
  };

  setIsLoading(true);
  requestBackend(params).then((response) => {
      setPage(response.data);
    })
    .finally(() => {
      setIsLoading(false);
    });
},[controlComponentesData]);

  useEffect(() => {
    getProducts();
  }, [getProducts]);

  return (
    <div className="container my-4 catalog-container">
      <div className="row catalog-title-container">
        <h1>Catálogo de produtos</h1>
      </div>
      <div className="row">
        {isLoading ? <CardLoader /> : (
          page?.content.map((product) => (
          <div className="col-sm-6 col-lg-4 col-xl-3" key={product.id}>
            <Link to={{ pathname: `/products/${product.id}` }}>
              <ProductCard product={product} />
            </Link>
          </div>
        )))}
      </div>
      <div className="row">
      <Pagination 
      pageCount={(page) ? page.totalPages : 0}
      range={(page) ? page.size : 0}
      onChange={handlePageChange}
      />
      
      </div>
    </div>
  );
};
export default Catalog;
