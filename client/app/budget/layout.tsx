import Sidebar from "@/app/_components/Sidebar";

const Layout: React.FC<{ children: React.ReactNode}> = ({ children }) => {
  return (
    <div>
      <Sidebar />
      {children}
    </div>
  )
}

export default Layout;
