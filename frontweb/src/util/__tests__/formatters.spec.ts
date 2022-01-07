import { formatPrice } from "util/formatters";

describe('formatePrice for positive numbers', () => {

    test('formatprice should format number pt-BR when given 10.1', () => {
        const result = formatPrice(10.1);
        expect(result).toEqual("10,10");
    });
  
    test('formatprice should format number pt-BR when given 0.1', () => {
        const result = formatPrice(0.1);
        expect(result).toEqual("0,10");
    });
})

