import { render, screen } from "@testing-library/react";
import { Product } from "types/product";
import { formatPrice } from "util/formatters";
import ProductCard from "..";

test('should render ProductCard', () => {

    const product: Product = {
        name: 'Computador',
        price: 120.00,
        imgUrl: 'https://google.com'
    } as Product;

    render(
        <ProductCard product={product} />

    );

    expect(screen.getByText(product.name)).toBeInTheDocument();
    expect(screen.getByAltText(product.name)).toBeInTheDocument();
    expect(screen.getByText("R$")).toBeInTheDocument();
    expect(screen.getByText("120,00")).toBeInTheDocument();
})