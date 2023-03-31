import Home, {
	FIRST_PAGE,
	getStaticProps as getDefaultStaticProps,
	Prop,
} from "./[...id]";

export const Index = (prop: Prop) => Home(prop);

export const getStaticProps = async () =>
	getDefaultStaticProps({ params: { id: [FIRST_PAGE()] } });

export default Index;
