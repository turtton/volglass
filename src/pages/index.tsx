import Home, {
	FIRST_PAGE,
	getStaticProps as getDefaultStaticProps,
	Prop,
} from "./[...id]";

export const Index = (prop: Prop) => Home(prop);

/**
 * This id is related to FIRST_PAGE()
 * {@link FIRST_PAGE}
 */
export const getStaticProps = async () =>
	getDefaultStaticProps({ params: { id: ["README"] } });

export default Index;
