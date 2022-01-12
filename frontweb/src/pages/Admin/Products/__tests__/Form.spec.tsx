import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Router, useParams } from "react-router-dom";
import history from "util/history";
import Form from "../Form";
import { ProductResponse, server } from "./fixtures";
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

        const submitButton = screen.getByRole('button', { name: /salvar/i })

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

    test('Should show 5 validation messages when just clicking submit', async () => {

        render(
            <Router history={history}>
                <Form />
            </Router>
        );

        const submitButton = screen.getByRole('button', { name: /salvar/i });

        userEvent.click(submitButton);

        await waitFor(() => {
            const messages = screen.getAllByText('Campo obrigatório')
            expect(messages).toHaveLength(5);
        })
    });

    test('Should clear validation messages when filling out the form correctly', async () => {

        render(
            <Router history={history}>
                <Form />
            </Router>
        );

        const submitButton = screen.getByRole('button', { name: /salvar/i });

        userEvent.click(submitButton);

        await waitFor(() => {
            const messages = screen.getAllByText('Campo obrigatório')
            expect(messages).toHaveLength(5);
        })

        const nameInput = screen.getByTestId('name');
        const priceInput = screen.getByTestId('price');
        const imgURLInput = screen.getByTestId('imgUrl');
        const descriptionInput = screen.getByTestId('description');
        const categoriesInput = screen.getByLabelText('Categorias');

        await selectEvent.select(categoriesInput, ['Eletrônicos', 'Computadores']);
        userEvent.type(nameInput, 'Computador');
        userEvent.type(priceInput, '5000.12');
        userEvent.type(imgURLInput, 'https://i.dell.com/is/image/DellContent//content/dam/global-asset-library/Products/Enterprise_Servers/PowerEdge/t140/dellemc_pet140_bezel_lf.psd?fmt=pjpg&pscan=auto&scl=1&wid=1738&hei=1894&qlt=100,0&resMode=sharp2&size=1738,1894');
        userEvent.type(descriptionInput, 'Excelente esse computador');

        await waitFor(() => {
            const messages = screen.queryAllByText('Campo obrigatório')
            expect(messages).toHaveLength(0);
        })
    });
});

describe('Product form update tests', () => {

    beforeEach(() => {
        (useParams as jest.Mock).mockReturnValue({
            productId: '2'
        });
    });

    test('Should show toast and redirect when submit form correctly', async () => {

        render(
            <Router history={history}>
                <Form />
                <ToastContainer />
            </Router>
        );

        await waitFor(() => {
            const nameInput = screen.getByTestId('name');
            const priceInput = screen.getByTestId('price');
            const imgURLInput = screen.getByTestId('imgUrl');
            const descriptionInput = screen.getByTestId('description');

            const formElement = screen.getByTestId('form');

            expect(nameInput).toHaveValue(ProductResponse.name);
            expect(priceInput).toHaveValue(String(ProductResponse.price));
            expect(imgURLInput).toHaveValue(ProductResponse.imgUrl);
            expect(descriptionInput).toHaveValue(ProductResponse.description);

            const ids = ProductResponse.categories.map(x => String(x.id));
            expect(formElement).toHaveFormValues({categories: ids});

        });

        const submitButton = screen.getByRole('button', { name: /salvar/i });

        userEvent.click(submitButton);

        await waitFor(() => {
            const toastElement = screen.getByText('Produto cadastrado com sucesso');
            expect(toastElement).toBeInTheDocument();
        });

        expect(history.location.pathname).toEqual('/admin/products');
    });
});