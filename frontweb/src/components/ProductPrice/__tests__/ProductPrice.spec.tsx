import { render, screen } from "@testing-library/react";
import { formatPrice } from "util/formatters";
import ProductPrice from "..";


test('should render ProductPrice', () => {
    
    const price = 120.00;

render(
    <ProductPrice price={price} />
);
   
    expect(screen.getByText("R$")).toBeInTheDocument();
    expect(screen.getByText("120,00")).toBeInTheDocument();
})