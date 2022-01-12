import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Router, useParams } from "react-router-dom";
import history from "util/history";
import Form from "../Form";
import { server } from "./fixtures";
import selectEvent from "react-select-event";
import { ToastContainer } from "react-toastify";

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useParams: jest.fn()
}));

describe('Product form create tests', () => {

    beforeEach(() => {
        (useParams as jest.Mock).mockReturnValue({
            productId: 'create'
        })
    })

    test('Should show toast and redirect when submit form correctly', async () => {

        render(
            <Router history={history}>
                <Form />
                <ToastContainer />
            </Router>
        );
        
        const nameInput = screen.getByTestId('name');
        const priceInput = screen.getByTestId('price');
        const imgURLInput = screen.getByTestId('imgUrl');
        const descriptionInput = screen.getByTestId('description');
        const categoriesInput = screen.getByLabelText('Categorias');

        const submitButton = screen.getByRole('button', { name: /salvar/i})

        await selectEvent.select(categoriesInput, ['Eletrônicos', 'Computadores']);
        userEvent.type(nameInput, 'Computador');
        userEvent.type(priceInput, '5000.12');
        userEvent.type(imgURLInput, 'https://i.dell.com/is/image/DellContent//content/dam/global-asset-library/Products/Enterprise_Servers/PowerEdge/t140/dellemc_pet140_bezel_lf.psd?fmt=pjpg&pscan=auto&scl=1&wid=1738&hei=1894&qlt=100,0&resMode=sharp2&size=1738,1894');
        userEvent.type(descriptionInput, 'Excelente esse computador');

        userEvent.click(submitButton);

        await waitFor(() => {
        const toastElement = screen.getByText('Produto cadastrado com sucesso');
        expect(toastElement).toBeInTheDocument();
        });

        expect(history.location.pathname).toEqual('/admin/products');
    });
});

